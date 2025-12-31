package com.incident.incidentservice.service;

import com.incident.incidentservice.dto.*;
import com.incident.incidentservice.entity.Incident;
import com.incident.incidentservice.entity.User;
import com.incident.incidentservice.enums.IncidentStatus;
import com.incident.incidentservice.enums.Severity;
import com.incident.incidentservice.event.IncidentEvent;
import com.incident.incidentservice.exception.IncidentNotFoundException;
import com.incident.incidentservice.exception.InvalidStatusTransitionException;
import com.incident.incidentservice.kafka.IncidentEventProducer;
import com.incident.incidentservice.mapper.IncidentMapper;
import com.incident.incidentservice.repository.IncidentRepository;
import com.incident.incidentservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;
    private final IncidentMapper mapper;
    private final IncidentEventProducer eventProducer;
    private final IncidentCacheService cacheService;
    private static final AtomicLong counter = new AtomicLong(0);

    @PostConstruct
    public void initCounter() {
        long count = incidentRepository.count();
        counter.set(count);
        log.info("✅ Initialized incident counter to: {}", count);
    }

    @Transactional
    public IncidentResponse createIncident(CreateIncidentRequest request) {
        log.info("Creating incident: {}", request.getTitle());

        Incident incident = Incident.builder()
                .incidentNumber(generateIncidentNumber())
                .title(request.getTitle())
                .description(request.getDescription())
                .severity(request.getSeverity())
                .status(IncidentStatus.TRIGGERED)
                .teamId(request.getTeamId())
                .build();

        if (request.getAssigneeId() != null) {
            userRepository.findById(request.getAssigneeId()).ifPresent(user -> {
                incident.setAssigneeId(user.getId());
                incident.setAssigneeName(user.getUsername());
                incident.setTeamId(user.getTeamId());
                incident.setTeamName(user.getTeamName());
            });
        }

        Incident saved = incidentRepository.save(incident);
        log.info("Incident created: {}", saved.getIncidentNumber());

        publishEvent("CREATED", saved, null);

        IncidentResponse response = mapper.toResponse(saved);
        cacheService.cacheIncident(response);

        return response;
    }

    public IncidentResponse getById(Long id) {
        // 1. Check cache first
        IncidentResponse cached = cacheService.getFromCache(id);
        if (cached != null) {
            return cached;
        }

        // 2. Not in cache, get from database
        Incident incident = findIncidentById(id);
        IncidentResponse response = mapper.toResponse(incident);

        // 3. Store in cache for next time
        cacheService.cacheIncident(response);

        return response;
    }

    public PagedResponse<IncidentResponse> getAll(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Page<Incident> incidents = incidentRepository.findAll(PageRequest.of(page, size, sort));
        return buildPagedResponse(incidents);
    }

    public PagedResponse<IncidentResponse> getByFilters(IncidentStatus status, Severity severity, Long assigneeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Incident> incidents = incidentRepository.findByFilters(status, severity, assigneeId, pageable);
        return buildPagedResponse(incidents);
    }

    public List<IncidentResponse> getActiveIncidents() {
        List<IncidentStatus> activeStatuses = List.of(
                IncidentStatus.TRIGGERED, IncidentStatus.ACKNOWLEDGED,
                IncidentStatus.INVESTIGATING, IncidentStatus.ESCALATED);
        return incidentRepository.findByStatusIn(activeStatuses).stream()
                .map(mapper::toResponse).toList();
    }

    @Transactional
    public IncidentResponse update(Long id, UpdateIncidentRequest request) {
        Incident incident = findIncidentById(id);

        if (request.getTitle() != null) incident.setTitle(request.getTitle());
        if (request.getDescription() != null) incident.setDescription(request.getDescription());
        if (request.getSeverity() != null) incident.setSeverity(request.getSeverity());
        if (request.getAssigneeId() != null) {
            userRepository.findById(request.getAssigneeId()).ifPresent(user -> {
                incident.setAssigneeId(user.getId());
                incident.setAssigneeName(user.getUsername());
            });
        }
        if (request.getTeamId() != null) incident.setTeamId(request.getTeamId());

        Incident saved = incidentRepository.save(incident);
        publishEvent("UPDATED", saved, null);

        cacheService.evictFromCache(id);

        return mapper.toResponse(saved);
    }

    @Transactional
    public IncidentResponse acknowledge(Long id, Long userId) {
        Incident incident = findIncidentById(id);
        IncidentStatus previousStatus = incident.getStatus();
        validateTransition(previousStatus, IncidentStatus.ACKNOWLEDGED);

        incident.setStatus(IncidentStatus.ACKNOWLEDGED);
        incident.setAcknowledgedAt(LocalDateTime.now());

        if (userId != null) {
            userRepository.findById(userId).ifPresent(user -> {
                incident.setAssigneeId(user.getId());
                incident.setAssigneeName(user.getUsername());
            });
        }

        Incident saved = incidentRepository.save(incident);
        log.info("Incident {} acknowledged", incident.getIncidentNumber());

        publishEvent("ACKNOWLEDGED", saved, previousStatus);

        cacheService.evictFromCache(id);

        return mapper.toResponse(saved);
    }

    @Transactional
    public IncidentResponse resolve(Long id, String resolution) {
        Incident incident = findIncidentById(id);
        IncidentStatus previousStatus = incident.getStatus();
        validateTransition(previousStatus, IncidentStatus.RESOLVED);

        incident.setStatus(IncidentStatus.RESOLVED);
        incident.setResolvedAt(LocalDateTime.now());
        if (resolution != null) {
            incident.setDescription(incident.getDescription() + "\n\nResolution: " + resolution);
        }

        Incident saved = incidentRepository.save(incident);
        log.info("Incident {} resolved", incident.getIncidentNumber());

        publishEvent("RESOLVED", saved, previousStatus);

        cacheService.evictFromCache(id);

        return mapper.toResponse(saved);
    }

    @Transactional
    public IncidentResponse close(Long id) {
        Incident incident = findIncidentById(id);
        IncidentStatus previousStatus = incident.getStatus();
        if (previousStatus != IncidentStatus.RESOLVED) {
            throw new InvalidStatusTransitionException(previousStatus, IncidentStatus.CLOSED);
        }

        incident.setStatus(IncidentStatus.CLOSED);
        incident.setClosedAt(LocalDateTime.now());

        Incident saved = incidentRepository.save(incident);
        log.info("Incident {} closed", incident.getIncidentNumber());

        publishEvent("CLOSED", saved, previousStatus);

        cacheService.evictFromCache(id);

        return mapper.toResponse(saved);
    }

    @Transactional
    public IncidentResponse assign(Long id, Long assigneeId) {
        Incident incident = findIncidentById(id);
        User user = userRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("User not found: " + assigneeId));

        incident.setAssigneeId(user.getId());
        incident.setAssigneeName(user.getUsername());
        incident.setTeamId(user.getTeamId());
        incident.setTeamName(user.getTeamName());

        Incident saved = incidentRepository.save(incident);
        log.info("Incident {} assigned to {}", incident.getIncidentNumber(), user.getUsername());

        publishEvent("ASSIGNED", saved, null);

        cacheService.evictFromCache(id);

        return mapper.toResponse(saved);
    }

    @Transactional
    public IncidentResponse escalate(Long id) {
        Incident incident = findIncidentById(id);
        IncidentStatus previousStatus = incident.getStatus();

        incident.setStatus(IncidentStatus.ESCALATED);
        incident.setEscalationLevel(incident.getEscalationLevel() + 1);
        incident.setSlaBreach(true);

        Incident saved = incidentRepository.save(incident);
        log.warn("Incident {} escalated to level {}", incident.getIncidentNumber(), incident.getEscalationLevel());

        publishEvent("ESCALATED", saved, previousStatus);

        cacheService.evictFromCache(id);

        return mapper.toResponse(saved);
    }

    private Incident findIncidentById(Long id) {
        return incidentRepository.findById(id).orElseThrow(() -> new IncidentNotFoundException(id));
    }

    private void validateTransition(IncidentStatus current, IncidentStatus next) {
        if (!current.canTransitionTo(next)) {
            throw new InvalidStatusTransitionException(current, next);
        }
    }

    private String generateIncidentNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("INC-%s-%04d", date, counter.incrementAndGet());
    }

    private PagedResponse<IncidentResponse> buildPagedResponse(Page<Incident> page) {
        return PagedResponse.<IncidentResponse>builder()
                .content(page.getContent().stream().map(mapper::toResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private void publishEvent(String eventType, Incident incident, IncidentStatus previousStatus) {
        IncidentEvent event = IncidentEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .incidentId(incident.getId())
                .incidentNumber(incident.getIncidentNumber())
                .title(incident.getTitle())
                .severity(incident.getSeverity().name())
                .previousStatus(previousStatus)
                .newStatus(incident.getStatus())
                .timestamp(LocalDateTime.now())
                .build();
        eventProducer.sendEvent(event);
    }
}