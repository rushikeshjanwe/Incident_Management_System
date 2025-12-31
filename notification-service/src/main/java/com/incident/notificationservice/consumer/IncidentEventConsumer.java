package com.incident.notificationservice.consumer;

import com.incident.notificationservice.event.IncidentEvent;
import com.incident.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IncidentEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "incident-event", groupId = "notification-group")
    public void consume(IncidentEvent event) {
        log.info("📥 Received event: {} for incident: {}", event.getEventType(), event.getIncidentNumber());
        notificationService.processEvent(event);
    }
}
