---

## 🔧 Postman Testing Guide

### Step 1: Download Postman Collection

Import this collection into Postman or create requests manually.

### Step 2: Setup Environment Variables

In Postman, create environment variables:

| Variable | Initial Value |
|----------|---------------|
| `base_url` | `http://localhost:8081` |
| `token` | (leave empty, will be set after login) |

### Step 3: Test Authentication

#### 3.1 Register User
```
Method: POST
URL: {{base_url}}/api/auth/register
Headers:
    Content-Type: application/json
Body (raw JSON):
{
    "username": "admin",
    "email": "admin@test.com",
    "password": "admin123",
    "role": "ADMIN"
}
```

**Expected Response (200 OK):**
```json
{
    "success": true,
    "data": {
        "token": "eyJhbGciOiJIUzM4NCJ9...",
        "username": "admin",
        "email": "admin@test.com",
        "role": "ADMIN"
    }
}
```

**Copy the token and save it in Postman environment variable `token`**

#### 3.2 Login
```
Method: POST
URL: {{base_url}}/api/auth/login
Headers:
    Content-Type: application/json
Body (raw JSON):
{
    "username": "admin",
    "password": "admin123"
}
```

#### 3.3 Auto-Save Token (Postman Script)

Add this to **Tests** tab in Login request:
```javascript
var jsonData = pm.response.json();
if (jsonData.success) {
    pm.environment.set("token", jsonData.data.token);
    console.log("Token saved!");
}
```

### Step 4: Test Incident APIs

#### 4.1 Create Incident
```
Method: POST
URL: {{base_url}}/api/incidents
Headers:
    Content-Type: application/json
    Authorization: Bearer {{token}}
Body (raw JSON):
{
    "title": "Production Database Down",
    "description": "MySQL server not responding on prod-db-01",
    "severity": "P1"
}
```

**Expected Response (201 Created):**
```json
{
    "success": true,
    "data": {
        "id": 1,
        "incidentNumber": "INC-20260113-0001",
        "title": "Production Database Down",
        "severity": "P1",
        "status": "TRIGGERED",
        "createdAt": "2026-01-13T10:30:00Z"
    },
    "message": "Incident created"
}
```

#### 4.2 Get All Incidents
```
Method: GET
URL: {{base_url}}/api/incidents?page=0&size=10
Headers:
    Authorization: Bearer {{token}}
```

#### 4.3 Get Incident by ID
```
Method: GET
URL: {{base_url}}/api/incidents/1
Headers:
    Authorization: Bearer {{token}}
```

#### 4.4 Acknowledge Incident
```
Method: PATCH
URL: {{base_url}}/api/incidents/1/acknowledge
Headers:
    Authorization: Bearer {{token}}
```

#### 4.5 Resolve Incident
```
Method: PATCH
URL: {{base_url}}/api/incidents/1/resolve?resolution=Restarted database server
Headers:
    Authorization: Bearer {{token}}
```

#### 4.6 Close Incident
```
Method: PATCH
URL: {{base_url}}/api/incidents/1/close
Headers:
    Authorization: Bearer {{token}}
```

#### 4.7 Escalate Incident
```
Method: PATCH
URL: {{base_url}}/api/incidents/1/escalate
Headers:
    Authorization: Bearer {{token}}
```

#### 4.8 Assign Incident
```
Method: PATCH
URL: {{base_url}}/api/incidents/1/assign?assigneeId=2
Headers:
    Authorization: Bearer {{token}}
```

#### 4.9 Filter Incidents
```
Method: GET
URL: {{base_url}}/api/incidents/filter?status=TRIGGERED&severity=P1
Headers:
    Authorization: Bearer {{token}}
```

#### 4.10 Get Active Incidents
```
Method: GET
URL: {{base_url}}/api/incidents/active
Headers:
    Authorization: Bearer {{token}}
```

### Step 5: Postman Collection JSON

<details>
<summary>Click to expand - Import this JSON into Postman</summary>
```json
{
    "info": {
        "name": "Incident Management System",
        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
    },
    "item": [
        {
            "name": "Auth",
            "item": [
                {
                    "name": "Register",
                    "request": {
                        "method": "POST",
                        "header": [
                            {
                                "key": "Content-Type",
                                "value": "application/json"
                            }
                        ],
                        "body": {
                            "mode": "raw",
                            "raw": "{\n    \"username\": \"admin\",\n    \"email\": \"admin@test.com\",\n    \"password\": \"admin123\",\n    \"role\": \"ADMIN\"\n}"
                        },
                        "url": {
                            "raw": "{{base_url}}/api/auth/register",
                            "host": ["{{base_url}}"],
                            "path": ["api", "auth", "register"]
                        }
                    }
                },
                {
                    "name": "Login",
                    "event": [
                        {
                            "listen": "test",
                            "script": {
                                "exec": [
                                    "var jsonData = pm.response.json();",
                                    "if (jsonData.success) {",
                                    "    pm.environment.set(\"token\", jsonData.data.token);",
                                    "}"
                                ]
                            }
                        }
                    ],
                    "request": {
                        "method": "POST",
                        "header": [
                            {
                                "key": "Content-Type",
                                "value": "application/json"
                            }
                        ],
                        "body": {
                            "mode": "raw",
                            "raw": "{\n    \"username\": \"admin\",\n    \"password\": \"admin123\"\n}"
                        },
                        "url": {
                            "raw": "{{base_url}}/api/auth/login",
                            "host": ["{{base_url}}"],
                            "path": ["api", "auth", "login"]
                        }
                    }
                }
            ]
        },
        {
            "name": "Incidents",
            "item": [
                {
                    "name": "Create Incident",
                    "request": {
                        "method": "POST",
                        "header": [
                            {
                                "key": "Content-Type",
                                "value": "application/json"
                            },
                            {
                                "key": "Authorization",
                                "value": "Bearer {{token}}"
                            }
                        ],
                        "body": {
                            "mode": "raw",
                            "raw": "{\n    \"title\": \"Server Down\",\n    \"description\": \"Production server not responding\",\n    \"severity\": \"P1\"\n}"
                        },
                        "url": {
                            "raw": "{{base_url}}/api/incidents",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents"]
                        }
                    }
                },
                {
                    "name": "Get All Incidents",
                    "request": {
                        "method": "GET",
                        "header": [
                            {
                                "key": "Authorization",
                                "value": "Bearer {{token}}"
                            }
                        ],
                        "url": {
                            "raw": "{{base_url}}/api/incidents",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents"]
                        }
                    }
                },
                {
                    "name": "Get Incident by ID",
                    "request": {
                        "method": "GET",
                        "header": [
                            {
                                "key": "Authorization",
                                "value": "Bearer {{token}}"
                            }
                        ],
                        "url": {
                            "raw": "{{base_url}}/api/incidents/1",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents", "1"]
                        }
                    }
                },
                {
                    "name": "Acknowledge Incident",
                    "request": {
                        "method": "PATCH",
                        "header": [
                            {
                                "key": "Authorization",
                                "value": "Bearer {{token}}"
                            }
                        ],
                        "url": {
                            "raw": "{{base_url}}/api/incidents/1/acknowledge",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents", "1", "acknowledge"]
                        }
                    }
                },
                {
                    "name": "Resolve Incident",
                    "request": {
                        "method": "PATCH",
                        "header": [
                            {
                                "key": "Authorization",
                                "value": "Bearer {{token}}"
                            }
                        ],
                        "url": {
                            "raw": "{{base_url}}/api/incidents/1/resolve?resolution=Fixed the issue",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents", "1", "resolve"],
                            "query": [
                                {
                                    "key": "resolution",
                                    "value": "Fixed the issue"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "Close Incident",
                    "request": {
                        "method": "PATCH",
                        "header": [
                            {
                                "key": "Authorization",
                                "value": "Bearer {{token}}"
                            }
                        ],
                        "url": {
                            "raw": "{{base_url}}/api/incidents/1/close",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents", "1", "close"]
                        }
                    }
                }
            ]
        }
    ]
}
```

</details>

---

## 🐛 Production-Level Debugging Guide

### Understanding Error Types
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         ERROR CATEGORIES                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  4xx ERRORS (Client's Fault)                                               │
│  ├── 400 Bad Request      → Invalid input data                            │
│  ├── 401 Unauthorized     → Missing or invalid token                      │
│  ├── 403 Forbidden        → Token valid but no permission                 │
│  ├── 404 Not Found        → Resource doesn't exist                        │
│  └── 422 Unprocessable    → Validation failed                             │
│                                                                             │
│  5xx ERRORS (Server's Fault)                                               │
│  ├── 500 Internal Error   → Code bug or unhandled exception               │
│  ├── 502 Bad Gateway      → Upstream service down                         │
│  ├── 503 Service Unavailable → Server overloaded                          │
│  └── 504 Gateway Timeout  → Request took too long                         │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Common Errors & How to Fix

#### Error 1: 401 Unauthorized
```json
{
    "status": 401,
    "error": "Unauthorized",
    "message": "Full authentication is required"
}
```

**Causes:**
1. No Authorization header
2. Token expired
3. Token malformed

**How to Debug:**
```bash
# Check if token is present
echo $TOKEN

# Decode JWT token (use jwt.io or)
# Check expiration time in payload
```

**Fix:**
```bash
# Get new token
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

---

#### Error 2: 403 Forbidden
```json
{
    "status": 403,
    "error": "Forbidden",
    "message": "Access Denied"
}
```

**Causes:**
1. User doesn't have required role
2. CORS issue (frontend)
3. Swagger URL not whitelisted

**How to Debug:**
```bash
# Check user role in token
# Decode JWT at jwt.io
# Look for "role" in payload

# Check SecurityConfig.java
# Verify URL is in permitAll() or correct role
```

**Fix:**
```java
// In SecurityConfig.java, add URL to permitAll
.requestMatchers("/your-url/**").permitAll()

// Or check user has correct role
.requestMatchers("/admin/**").hasRole("ADMIN")
```

---

#### Error 3: 500 Internal Server Error
```json
{
    "status": 500,
    "error": "Internal Server Error",
    "message": "Something went wrong"
}
```

**Causes:**
1. NullPointerException
2. Database connection failed
3. Kafka not available
4. Redis not available

**How to Debug:**
```bash
# Step 1: Check application logs
docker-compose logs incident-service --tail=100

# Step 2: Look for stack trace
# Find the FIRST line with YOUR package name
# Example: at com.incident.incidentservice.service.IncidentService.createIncident(IncidentService.java:45)

# Step 3: Check that specific line in code
```

**Common 500 Errors:**

| Error in Logs | Cause | Fix |
|---------------|-------|-----|
| `NullPointerException` | Variable is null | Add null check or fix initialization |
| `Connection refused :5432` | PostgreSQL down | Start PostgreSQL |
| `Connection refused :6379` | Redis down | Start Redis |
| `Connection refused :9092` | Kafka down | Start Kafka |
| `Table doesn't exist` | Migration not run | Check Hibernate ddl-auto |
| `Duplicate key` | Unique constraint violated | Check data or add validation |

---

#### Error 4: Connection Refused
```
org.postgresql.util.PSQLException: Connection to localhost:5432 refused
```

**How to Debug:**
```bash
# Step 1: Check if service is running
docker-compose ps

# Step 2: Check service logs
docker-compose logs postgres

# Step 3: Test connection manually
docker exec -it incident-postgres psql -U postgres -d incident_db

# Step 4: Check environment variables
docker exec incident-service env | grep DB_
```

**Fix:**
```bash
# Restart the service
docker-compose restart postgres

# Or recreate everything
docker-compose down
docker-compose up --build
```

---

#### Error 5: Kafka Not Receiving Messages

**Symptoms:**
- Incident created successfully
- But notification-service shows no logs

**How to Debug:**
```bash
# Step 1: Check Kafka is running
docker-compose ps | grep kafka

# Step 2: Check topic exists
docker exec -it incident-kafka kafka-topics.sh --list --bootstrap-server localhost:9092

# Step 3: Check incident-service producer logs
docker-compose logs incident-service | grep -i kafka

# Step 4: Check notification-service consumer logs
docker-compose logs notification-service | grep -i kafka

# Step 5: Manually produce a message
docker exec -it incident-kafka kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic incident-events

# Step 6: Manually consume messages
docker exec -it incident-kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic incident-events \
  --from-beginning
```

**Common Kafka Issues:**

| Issue | Cause | Fix |
|-------|-------|-----|
| Topic not found | Auto-create disabled | Create topic manually or enable auto-create |
| Serialization error | Event class mismatch | Check IncidentEvent class in both services |
| Connection refused | Wrong bootstrap server | Check KAFKA_SERVERS env variable |
| Consumer not receiving | Wrong group ID | Check consumer group configuration |

---

#### Error 6: Redis Cache Issues

**Symptoms:**
- First request slow (50ms)
- Second request also slow (should be 1-2ms)

**How to Debug:**
```bash
# Step 1: Check Redis is running
docker-compose ps | grep redis

# Step 2: Connect to Redis
docker exec -it incident-redis redis-cli

# Step 3: Check if keys exist
127.0.0.1:6379> KEYS *

# Step 4: Check specific cache
127.0.0.1:6379> GET incident:1

# Step 5: Check Redis logs
docker-compose logs incident-redis
```

**Fix:**
```bash
# Clear Redis cache
docker exec -it incident-redis redis-cli FLUSHALL

# Restart Redis
docker-compose restart incident-redis
```

---

### Production Debugging Checklist
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    🔍 DEBUGGING CHECKLIST                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  □ 1. Check if ALL services are running                                    │
│       docker-compose ps                                                    │
│                                                                             │
│  □ 2. Check application logs for errors                                    │
│       docker-compose logs <service-name> --tail=100                        │
│                                                                             │
│  □ 3. Check health endpoint                                                │
│       curl http://localhost:8081/actuator/health                           │
│                                                                             │
│  □ 4. Check database connection                                            │
│       docker exec -it incident-postgres psql -U postgres -c "SELECT 1"     │
│                                                                             │
│  □ 5. Check Redis connection                                               │
│       docker exec -it incident-redis redis-cli PING                        │
│                                                                             │
│  □ 6. Check Kafka connection                                               │
│       docker exec -it incident-kafka kafka-topics.sh --list \              │
│         --bootstrap-server localhost:9092                                  │
│                                                                             │
│  □ 7. Check environment variables                                          │
│       docker exec incident-service env                                     │
│                                                                             │
│  □ 8. Check network connectivity                                           │
│       docker network ls                                                    │
│       docker network inspect incident-management-system_default            │
│                                                                             │
│  □ 9. Check resource usage                                                 │
│       docker stats                                                         │
│                                                                             │
│  □ 10. Check disk space                                                    │
│        docker system df                                                    │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

### Real-World Debugging Scenarios

#### Scenario 1: API Suddenly Stopped Working

**Symptoms:** All APIs return 500 error

**Debug Steps:**
```bash
# 1. Check logs
docker-compose logs incident-service --tail=50

# 2. Found: "Connection pool exhausted"

# 3. Check database connections
docker exec -it incident-postgres psql -U postgres -c "SELECT count(*) FROM pg_stat_activity;"

# 4. Found: 100 connections (max is 100)

# 5. Root Cause: Connection leak in code

# 6. Temporary Fix: Restart service
docker-compose restart incident-service

# 7. Permanent Fix: Add connection timeout in application.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      connection-timeout: 30000
```

---

#### Scenario 2: Slow API Response

**Symptoms:** GET /incidents/{id} taking 5 seconds

**Debug Steps:**
```bash
# 1. Check if cache is working
docker exec -it incident-redis redis-cli KEYS "*"

# 2. Found: No keys (cache is empty)

# 3. Check Redis logs
docker-compose logs incident-redis

# 4. Found: Redis restarted, cache cleared

# 5. Check IncidentCacheService.java

# 6. Found: Cache eviction on every update (bug)

# 7. Fix: Only evict specific key, not all keys
```

---

#### Scenario 3: Notifications Not Sending

**Symptoms:** Incident created but no notification

**Debug Steps:**
```bash
# 1. Check notification-service logs
docker-compose logs notification-service --tail=50

# 2. Found: "Deserialization error"

# 3. Check event class in both services
# incident-service: IncidentEvent has 5 fields
# notification-service: IncidentEvent has 4 fields

# 4. Root Cause: Field mismatch after code update

# 5. Fix: Update notification-service IncidentEvent class

# 6. Rebuild
mvn clean package -DskipTests
docker-compose up --build
```

---

#### Scenario 4: Memory Issues

**Symptoms:** Service crashes after few hours

**Debug Steps:**
```bash
# 1. Check memory usage
docker stats

# 2. Found: incident-service using 2GB (limit is 512MB)

# 3. Check for memory leaks
# Add JVM flags to see heap usage

# 4. In Dockerfile, add:
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]

# 5. Common causes:
#    - Large result sets not paginated
#    - Caching too much data
#    - Event listeners not cleaned up
```

---

### Logging Best Practices
```java
// Good logging examples
log.info("Creating incident: title={}, severity={}", request.getTitle(), request.getSeverity());
log.debug("Cache HIT for incident ID: {}", id);
log.warn("Incident {} escalated to level {}", incident.getIncidentNumber(), level);
log.error("Failed to send notification for incident {}: {}", incidentId, e.getMessage(), e);

// Bad logging (don't do this)
log.info("Creating incident");  // No context
log.info(request.toString());   // May expose sensitive data
log.error(e.toString());        // No stack trace
```

**Enable Debug Logging:**
```yaml
# application.yml
logging:
  level:
    com.incident: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql: TRACE
```

---

### Health Check Endpoints
```bash
# Application health
curl http://localhost:8081/actuator/health

# Detailed health (if enabled)
curl http://localhost:8081/actuator/health/db
curl http://localhost:8081/actuator/health/redis
curl http://localhost:8081/actuator/health/kafka

# Application info
curl http://localhost:8081/actuator/info

# Metrics
curl http://localhost:8081/actuator/metrics
curl http://localhost:8081/actuator/metrics/jvm.memory.used
```

---
