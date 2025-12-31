package com.incident.incidentservice.mapper;

import com.incident.incidentservice.dto.IncidentResponse;
import com.incident.incidentservice.entity.Incident;
import org.springframework.stereotype.Component;

@Component
public class IncidentMapper {

    public IncidentResponse toResponse(Incident incident) {
        return IncidentResponse.builder()
                .id(incident.getId())
                .incidentNumber(incident.getIncidentNumber())
                .title(incident.getTitle())
                .description(incident.getDescription())
                .severity(incident.getSeverity())
                .status(incident.getStatus())
                .assigneeId(incident.getAssigneeId())
                .assigneeName(incident.getAssigneeName())
                .teamId(incident.getTeamId())
                .teamName(incident.getTeamName())
                .escalationLevel(incident.getEscalationLevel())
                .slaBreach(incident.isSlaBreach())
                .createdAt(incident.getCreatedAt())
                .acknowledgedAt(incident.getAcknowledgedAt())
                .resolvedAt(incident.getResolvedAt())
                .closedAt(incident.getClosedAt())
                .build();
    }
}
