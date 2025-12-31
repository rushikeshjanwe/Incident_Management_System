package com.incident.incidentservice.dto;

import com.incident.incidentservice.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIncidentRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    @NotNull(message = "Severity is required")
    private Severity severity;
    private Long assigneeId;
    private Long teamId;
}
