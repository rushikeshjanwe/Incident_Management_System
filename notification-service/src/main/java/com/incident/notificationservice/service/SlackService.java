package com.incident.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SlackService {

    public void sendSlackMessage(String channel, String message) {
        // Mock - just log for now
        log.info("💬 SLACK MESSAGE SENT");
        log.info("   Channel: {}", channel);
        log.info("   Message: {}", message);
    }
}