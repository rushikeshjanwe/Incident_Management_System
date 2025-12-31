package com.incident.incidentservice.enums;

public enum IncidentStatus {
    TRIGGERED,
    ACKNOWLEDGED,
    INVESTIGATING,
    RESOLVED,
    CLOSED,
    ESCALATED;

    public boolean canTransitionTo(IncidentStatus next) {
        return switch (this) {
            case TRIGGERED -> next == ACKNOWLEDGED || next == ESCALATED;
            case ACKNOWLEDGED -> next == INVESTIGATING || next == RESOLVED || next == ESCALATED;
            case INVESTIGATING -> next == RESOLVED || next == ESCALATED;
            case ESCALATED -> next == ACKNOWLEDGED || next == INVESTIGATING || next == RESOLVED;
            case RESOLVED -> next == CLOSED || next == INVESTIGATING;
            case CLOSED -> false;
        };
    }
}
