package com.incident.incidentservice.dto;

import com.incident.incidentservice.enums.IncidentStatus;
import com.incident.incidentservice.enums.Severity;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentResponse {
    private Long id;
    private String incidentNumber;
    private String title;
    private String description;
    private Severity severity;
    private IncidentStatus status;
    private Long assigneeId;
    private String assigneeName;
    private Long teamId;
    private String teamName;
    private int escalationLevel;
    private boolean slaBreach;
    private LocalDateTime createdAt;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
}
