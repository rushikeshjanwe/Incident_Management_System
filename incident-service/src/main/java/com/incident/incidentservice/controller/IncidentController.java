package com.incident.incidentservice.controller;

import com.incident.incidentservice.dto.*;
import com.incident.incidentservice.enums.IncidentStatus;
import com.incident.incidentservice.enums.Severity;
import com.incident.incidentservice.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
@Tag(name = "Incidents", description = "Incident Management APIs - Create, Read, Update incidents")
@SecurityRequirement(name = "Bearer Authentication")
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    @Operation(
            summary = "Create a new incident",
            description = "Creates a new incident with TRIGGERED status. Sends notifications via Kafka."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Incident created successfully",
                    content = @Content(schema = @Schema(implementation = IncidentResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<IncidentResponse>> create(@Valid @RequestBody CreateIncidentRequest request) {
        IncidentResponse incident = incidentService.createIncident(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(incident, "Incident created"));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get incident by ID",
            description = "Returns a single incident by its ID. Uses Redis cache for faster retrieval."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved incident",
                    content = @Content(schema = @Schema(implementation = IncidentResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Incident not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<IncidentResponse>> getById(
            @Parameter(description = "Incident ID") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.getById(id)));
    }

    @GetMapping
    @Operation(
            summary = "Get all incidents",
            description = "Returns paginated list of all incidents. Supports sorting and pagination."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved incidents"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<PagedResponse<IncidentResponse>>> getAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.getAll(page, size, sortBy, sortDir)));
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Filter incidents",
            description = "Returns incidents filtered by status, severity, and/or assignee"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved filtered incidents"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<PagedResponse<IncidentResponse>>> getByFilters(
            @Parameter(description = "Filter by status") @RequestParam(required = false) IncidentStatus status,
            @Parameter(description = "Filter by severity") @RequestParam(required = false) Severity severity,
            @Parameter(description = "Filter by assignee ID") @RequestParam(required = false) Long assigneeId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.getByFilters(status, severity, assigneeId, page, size)));
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get active incidents",
            description = "Returns all incidents that are not CLOSED or RESOLVED"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved active incidents"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<List<IncidentResponse>>> getActive() {
        return ResponseEntity.ok(ApiResponse.success(incidentService.getActiveIncidents()));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update incident",
            description = "Updates an existing incident's details"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Incident updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Incident not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<IncidentResponse>> update(
            @Parameter(description = "Incident ID") @PathVariable Long id,
            @RequestBody UpdateIncidentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.update(id, request), "Incident updated"));
    }

    @PatchMapping("/{id}/acknowledge")
    @Operation(
            summary = "Acknowledge incident",
            description = "Changes incident status from TRIGGERED to ACKNOWLEDGED. Sends Slack notification."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Incident acknowledged successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid status transition"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Incident not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<IncidentResponse>> acknowledge(
            @Parameter(description = "Incident ID") @PathVariable Long id,
            @Parameter(description = "User ID acknowledging the incident") @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.acknowledge(id, userId), "Incident acknowledged"));
    }

    @PatchMapping("/{id}/resolve")
    @Operation(
            summary = "Resolve incident",
            description = "Changes incident status from ACKNOWLEDGED to RESOLVED. Requires resolution description."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Incident resolved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid status transition"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Incident not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<IncidentResponse>> resolve(
            @Parameter(description = "Incident ID") @PathVariable Long id,
            @Parameter(description = "Resolution description") @RequestParam(required = false) String resolution) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.resolve(id, resolution), "Incident resolved"));
    }

    @PatchMapping("/{id}/close")
    @Operation(
            summary = "Close incident",
            description = "Changes incident status from RESOLVED to CLOSED. Final state of incident lifecycle."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Incident closed successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid status transition"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Incident not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<IncidentResponse>> close(
            @Parameter(description = "Incident ID") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.close(id), "Incident closed"));
    }

    @PatchMapping("/{id}/assign")
    @Operation(
            summary = "Assign incident to user",
            description = "Assigns incident to a specific user for handling"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Incident assigned successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Incident or User not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<IncidentResponse>> assign(
            @Parameter(description = "Incident ID") @PathVariable Long id,
            @Parameter(description = "User ID to assign") @RequestParam Long assigneeId) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.assign(id, assigneeId), "Incident assigned"));
    }

    @PatchMapping("/{id}/escalate")
    @Operation(
            summary = "Escalate incident",
            description = "Escalates incident to next level. Sends urgent notifications via Email, SMS, and Slack."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Incident escalated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Cannot escalate - incident already closed/resolved"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Incident not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    public ResponseEntity<ApiResponse<IncidentResponse>> escalate(
            @Parameter(description = "Incident ID") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(incidentService.escalate(id), "Incident escalated"));
    }
}