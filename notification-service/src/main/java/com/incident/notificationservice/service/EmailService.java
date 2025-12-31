package com.incident.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    public void sendEmail(String to, String subject, String body) {
        // Mock - just log for now
        log.info("📧 EMAIL SENT");
        log.info("   To: {}", to);
        log.info("   Subject: {}", subject);
        log.info("   Body: {}", body);
    }
}