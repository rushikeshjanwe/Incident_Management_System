package com.incident.incidentservice.enums;

import lombok.Getter;

@Getter
public enum Severity {
    P1("Critical", 5, 60),
    P2("High", 15, 240),
    P3("Medium", 60, 1440),
    P4("Low", 240, 4320);

    private final String description;
    private final int ackSlaMinutes;
    private final int resolveSlaMinutes;

    Severity(String description, int ackSlaMinutes, int resolveSlaMinutes) {
        this.description = description;
        this.ackSlaMinutes = ackSlaMinutes;
        this.resolveSlaMinutes = resolveSlaMinutes;
    }
}
