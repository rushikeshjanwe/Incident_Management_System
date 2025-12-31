package com.incident.incidentservice.controller;

import com.incident.incidentservice.dto.*;
import com.incident.incidentservice.enums.IncidentStatus;
import com.incident.incidentservice.enums.Severity;
import com.incident.incidentservice.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    public ResponseEntity<ApiResponse<IncidentResponse>> create(@Valid @RequestBody CreateIncidentRequest request) {
        IncidentResponse incident = incidentService.createIncident(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(incident, "Incident created"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IncidentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<IncidentResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.getAll(page, size, sortBy, sortDir)));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<PagedResponse<IncidentResponse>>> getByFilters(
            @RequestParam(required = false) IncidentStatus status,
            @RequestParam(required = false) Severity severity,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.getByFilters(status, severity, assigneeId, page, size)));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<IncidentResponse>>> getActive() {
        return ResponseEntity.ok(ApiResponse.success(incidentService.getActiveIncidents()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IncidentResponse>> update(
            @PathVariable Long id, @RequestBody UpdateIncidentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.update(id, request), "Incident updated"));
    }

    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<ApiResponse<IncidentResponse>> acknowledge(
            @PathVariable Long id, @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.acknowledge(id, userId), "Incident acknowledged"));
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<IncidentResponse>> resolve(
            @PathVariable Long id, @RequestParam(required = false) String resolution) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.resolve(id, resolution), "Incident resolved"));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<ApiResponse<IncidentResponse>> close(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.close(id), "Incident closed"));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<IncidentResponse>> assign(
            @PathVariable Long id, @RequestParam Long assigneeId) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.assign(id, assigneeId), "Incident assigned"));
    }

    @PatchMapping("/{id}/escalate")
    public ResponseEntity<ApiResponse<IncidentResponse>> escalate(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.escalate(id), "Incident escalated"));
    }
}
