package com.incident.incidentservice.dto;

import com.incident.incidentservice.enums.Severity;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIncidentRequest {
    private String title;
    private String description;
    private Severity severity;
    private Long assigneeId;
    private Long teamId;
}
