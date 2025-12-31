package com.incident.notificationservice.event;

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
    private String previousStatus;
    private String newStatus;
    private LocalDateTime timestamp;
}