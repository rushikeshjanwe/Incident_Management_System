# Incident Management System - Phase 1

## What's Built
- ✅ incident-service with CRUD APIs
- ✅ PostgreSQL integration
- ✅ State machine for incident lifecycle
- ✅ Unit tests
- ✅ Docker setup

## Project Structure
```
incident-management-system/
├── incident-service/
│   ├── src/main/java/com/incident/incidentservice/
│   │   ├── controller/IncidentController.java
│   │   ├── service/IncidentService.java
│   │   ├── repository/IncidentRepository.java
│   │   ├── entity/Incident.java, User.java
│   │   ├── dto/CreateIncidentRequest, IncidentResponse, etc.
│   │   ├── enums/IncidentStatus.java, Severity.java
│   │   └── exception/GlobalExceptionHandler.java
│   ├── src/test/java/...
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml
└── README.md
```

## APIs Available

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/incidents | Create incident |
| GET | /api/incidents | List all (paginated) |
| GET | /api/incidents/{id} | Get by ID |
| GET | /api/incidents/active | Get active incidents |
| GET | /api/incidents/filter | Filter by status/severity |
| PUT | /api/incidents/{id} | Update incident |
| PATCH | /api/incidents/{id}/acknowledge | Acknowledge |
| PATCH | /api/incidents/{id}/resolve | Resolve |
| PATCH | /api/incidents/{id}/close | Close |
| PATCH | /api/incidents/{id}/assign | Assign to user |
| PATCH | /api/incidents/{id}/escalate | Escalate |

## How to Run

### Option 1: Docker (Recommended)
```bash
cd incident-management-system
docker-compose up -d
```

### Option 2: Local (requires PostgreSQL)
```bash
# Start PostgreSQL first, then:
cd incident-service
mvn spring-boot:run
```

## Test the APIs

### Create Incident
```bash
curl -X POST http://localhost:8081/api/incidents \
  -H "Content-Type: application/json" \
  -d '{"title": "Database down", "severity": "P1"}'
```

### Get All Incidents
```bash
curl http://localhost:8081/api/incidents
```

### Acknowledge Incident
```bash
curl -X PATCH http://localhost:8081/api/incidents/1/acknowledge
```

### Resolve Incident
```bash
curl -X PATCH "http://localhost:8081/api/incidents/1/resolve?resolution=Fixed%20connection%20pool"
```

## Next Phase
Phase 2: Add Kafka + notification-service for event-driven architecture
