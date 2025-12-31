package com.incident.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    public void sendSms(String phoneNumber, String message) {
        // Mock - just log for now
        log.info("📱 SMS SENT");
        log.info("   To: {}", phoneNumber);
        log.info("   Message: {}", message);
    }
}