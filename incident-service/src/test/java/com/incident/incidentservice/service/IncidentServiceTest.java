package com.incident.incidentservice.service;

import com.incident.incidentservice.dto.*;
import com.incident.incidentservice.entity.Incident;
import com.incident.incidentservice.enums.IncidentStatus;
import com.incident.incidentservice.enums.Severity;
import com.incident.incidentservice.exception.IncidentNotFoundException;
import com.incident.incidentservice.exception.InvalidStatusTransitionException;
import com.incident.incidentservice.mapper.IncidentMapper;
import com.incident.incidentservice.repository.IncidentRepository;
import com.incident.incidentservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IncidentMapper mapper;
    @InjectMocks
    private IncidentService incidentService;

    private Incident incident;
    private IncidentResponse response;

    @BeforeEach
    void setUp() {
        incident = Incident.builder()
                .id(1L)
                .incidentNumber("INC-001")
                .title("Test Incident")
                .severity(Severity.P1)
                .status(IncidentStatus.TRIGGERED)
                .build();

        response = IncidentResponse.builder()
                .id(1L)
                .incidentNumber("INC-001")
                .title("Test Incident")
                .severity(Severity.P1)
                .status(IncidentStatus.TRIGGERED)
                .build();
    }

    @Test
    void createIncident_Success() {
        CreateIncidentRequest request = CreateIncidentRequest.builder()
                .title("Test Incident")
                .severity(Severity.P1)
                .build();

        when(incidentRepository.save(any())).thenReturn(incident);
        when(mapper.toResponse(any())).thenReturn(response);

        IncidentResponse result = incidentService.createIncident(request);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Incident");
        verify(incidentRepository).save(any());
    }

    @Test
    void getById_Success() {
        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));
        when(mapper.toResponse(incident)).thenReturn(response);

        IncidentResponse result = incidentService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getById_NotFound() {
        when(incidentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> incidentService.getById(99L))
                .isInstanceOf(IncidentNotFoundException.class);
    }

    @Test
    void acknowledge_Success() {
        incident.setStatus(IncidentStatus.TRIGGERED);
        Incident acknowledged = Incident.builder()
                .id(1L).status(IncidentStatus.ACKNOWLEDGED).build();
        IncidentResponse ackResponse = IncidentResponse.builder()
                .id(1L).status(IncidentStatus.ACKNOWLEDGED).build();

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));
        when(incidentRepository.save(any())).thenReturn(acknowledged);
        when(mapper.toResponse(any())).thenReturn(ackResponse);

        IncidentResponse result = incidentService.acknowledge(1L, null);

        assertThat(result.getStatus()).isEqualTo(IncidentStatus.ACKNOWLEDGED);
    }

    @Test
    void acknowledge_InvalidTransition() {
        incident.setStatus(IncidentStatus.CLOSED);
        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));

        assertThatThrownBy(() -> incidentService.acknowledge(1L, null))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void resolve_Success() {
        incident.setStatus(IncidentStatus.ACKNOWLEDGED);
        Incident resolved = Incident.builder()
                .id(1L).status(IncidentStatus.RESOLVED).build();
        IncidentResponse resResponse = IncidentResponse.builder()
                .id(1L).status(IncidentStatus.RESOLVED).build();

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));
        when(incidentRepository.save(any())).thenReturn(resolved);
        when(mapper.toResponse(any())).thenReturn(resResponse);

        IncidentResponse result = incidentService.resolve(1L, "Fixed");

        assertThat(result.getStatus()).isEqualTo(IncidentStatus.RESOLVED);
    }

    @Test
    void escalate_Success() {
        incident.setEscalationLevel(0);
        Incident escalated = Incident.builder()
                .id(1L).status(IncidentStatus.ESCALATED).escalationLevel(1).slaBreach(true).build();
        IncidentResponse escResponse = IncidentResponse.builder()
                .id(1L).status(IncidentStatus.ESCALATED).escalationLevel(1).slaBreach(true).build();

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));
        when(incidentRepository.save(any())).thenReturn(escalated);
        when(mapper.toResponse(any())).thenReturn(escResponse);

        IncidentResponse result = incidentService.escalate(1L);

        assertThat(result.getStatus()).isEqualTo(IncidentStatus.ESCALATED);
        assertThat(result.getEscalationLevel()).isEqualTo(1);
        assertThat(result.isSlaBreach()).isTrue();
    }
}
