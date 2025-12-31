package com.incident.incidentservice.exception;

import com.incident.incidentservice.enums.IncidentStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(IncidentStatus from, IncidentStatus to) {
        super("Cannot transition from " + from + " to " + to);
    }
}
