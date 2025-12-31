package com.incident.notificationservice.service;

import com.incident.notificationservice.event.IncidentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;
    private final SmsService smsService;
    private final SlackService slackService;

    public void processEvent(IncidentEvent event) {
        log.info("Processing event: {} for incident: {}", event.getEventType(), event.getIncidentNumber());

        switch (event.getEventType()) {
            case "CREATED" -> handleCreated(event);
            case "ACKNOWLEDGED" -> handleAcknowledged(event);
            case "RESOLVED" -> handleResolved(event);
            case "ESCALATED" -> handleEscalated(event);
            case "CLOSED" -> handleClosed(event);
            default -> log.info("No notification for event type: {}", event.getEventType());
        }
    }

    private void handleCreated(IncidentEvent event) {
        String subject = String.format("🚨 NEW INCIDENT: %s [%s]", event.getIncidentNumber(), event.getSeverity());
        String body = String.format("Incident: %s\nTitle: %s\nSeverity: %s\nStatus: %s",
                event.getIncidentNumber(), event.getTitle(), event.getSeverity(), event.getNewStatus());

        emailService.sendEmail("oncall-team@company.com", subject, body);
        smsService.sendSms("+91-9999999999", subject);
        slackService.sendSlackMessage("#incidents", body);
    }

    private void handleAcknowledged(IncidentEvent event) {
        String message = String.format("✅ ACKNOWLEDGED: %s - %s", event.getIncidentNumber(), event.getTitle());
        slackService.sendSlackMessage("#incidents", message);
    }

    private void handleResolved(IncidentEvent event) {
        String subject = String.format("✅ RESOLVED: %s", event.getIncidentNumber());
        String body = String.format("Incident %s has been resolved.\nTitle: %s",
                event.getIncidentNumber(), event.getTitle());

        emailService.sendEmail("oncall-team@company.com", subject, body);
        slackService.sendSlackMessage("#incidents", body);
    }

    private void handleEscalated(IncidentEvent event) {
        String subject = String.format("⚠️ ESCALATED: %s [%s]", event.getIncidentNumber(), event.getSeverity());
        String body = String.format("URGENT! Incident escalated!\nIncident: %s\nTitle: %s\nSeverity: %s",
                event.getIncidentNumber(), event.getTitle(), event.getSeverity());

        emailService.sendEmail("manager@company.com", subject, body);
        smsService.sendSms("+91-8888888888", subject);
        slackService.sendSlackMessage("#incidents-urgent", body);
    }

    private void handleClosed(IncidentEvent event) {
        String message = String.format("📁 CLOSED: %s - %s", event.getIncidentNumber(), event.getTitle());
        slackService.sendSlackMessage("#incidents", message);
    }
}