package com.incident.incidentservice.kafka;

import com.incident.incidentservice.event.IncidentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentEventProducer {

    private final KafkaTemplate<String, IncidentEvent> kafkaTemplate;
    private static final String TOPIC = "incident-event";

    public void sendEvent(IncidentEvent event) {
        log.info("Sending event: {} for incident: {}", event.getEventType(), event.getIncidentNumber());
        kafkaTemplate.send(TOPIC, event.getIncidentNumber(), event);
    }
}