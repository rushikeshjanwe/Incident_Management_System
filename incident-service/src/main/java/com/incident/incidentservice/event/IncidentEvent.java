package com.incident.incidentservice.event;

import com.incident.incidentservice.enums.IncidentStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentEvent {
    private String eventId;
    private String eventType;
    private Long incidentId;
    private String incidentNumber;
    private String title;
    private String severity;
    private IncidentStatus previousStatus;
    private IncidentStatus newStatus;
    private LocalDateTime timestamp;
}