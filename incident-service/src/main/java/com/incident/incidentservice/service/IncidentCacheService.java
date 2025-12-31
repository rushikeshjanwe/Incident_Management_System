package com.incident.incidentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.incident.incidentservice.dto.IncidentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IncidentCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CACHE_PREFIX = "incident:";
    private static final long CACHE_TTL_MINUTES = 30;

    public IncidentCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void cacheIncident(IncidentResponse incident) {
        try {
            String key = CACHE_PREFIX + incident.getId();
            redisTemplate.opsForValue().set(key, incident, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            log.info("✅ Cached incident: {}", incident.getIncidentNumber());
        } catch (Exception e) {
            log.error("Failed to cache incident: {}", e.getMessage());
        }
    }

    public IncidentResponse getFromCache(Long id) {
        try {
            String key = CACHE_PREFIX + id;
            Object cached = redisTemplate.opsForValue().get(key);

            if (cached == null) {
                log.info("❌ Cache MISS for incident ID: {}", id);
                return null;
            }

            log.info("🎯 Cache HIT for incident ID: {}", id);

            // Convert to IncidentResponse
            if (cached instanceof IncidentResponse) {
                return (IncidentResponse) cached;
            }

            // Handle LinkedHashMap from Redis
            return objectMapper.convertValue(cached, IncidentResponse.class);

        } catch (Exception e) {
            log.error("Failed to get from cache: {}", e.getMessage());
            return null;
        }
    }

    public void evictFromCache(Long id) {
        try {
            String key = CACHE_PREFIX + id;
            redisTemplate.delete(key);
            log.info("🗑️ Evicted incident from cache: {}", id);
        } catch (Exception e) {
            log.error("Failed to evict from cache: {}", e.getMessage());
        }
    }
}