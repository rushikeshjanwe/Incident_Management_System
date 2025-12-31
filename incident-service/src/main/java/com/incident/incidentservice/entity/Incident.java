package com.incident.incidentservice.entity;

import com.incident.incidentservice.enums.IncidentStatus;
import com.incident.incidentservice.enums.Severity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String incidentNumber;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentStatus status;

    private Long assigneeId;
    private String assigneeName;
    private Long teamId;
    private String teamName;

    @Builder.Default
    private int escalationLevel = 0;

    @Builder.Default
    private boolean slaBreach = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime acknowledgedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;

    @PrePersist
    public void prePersist() {
        if (status == null) status = IncidentStatus.TRIGGERED;
    }
}
