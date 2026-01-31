# 🚨 Incident Management System

> A production-ready, microservices-based incident management platform built with modern technologies and best practices.

[![Build Status](https://github.com/YOUR_USERNAME/incident-management-system/actions/workflows/build.yml/badge.svg)](https://github.com/YOUR_USERNAME/incident-management-system/actions)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📋 Table of Contents

- [Quick Demo](#-quick-demo)
- [What is This](#-what-is-this)
- [Why I Built This](#-why-i-built-this)
- [What I Learned](#-what-i-learned)
- [Tech Stack](#️-tech-stack)
- [System Architecture](#️-system-architecture)
- [Project Structure](#-project-structure)
- [Features](#-features)
- [Getting Started](#-getting-started)
- [API Reference](#-api-reference)
- [Testing with Swagger](#-testing-with-swagger-ui)
- [Testing with Postman](#-testing-with-postman)
- [Testing with cURL](#-testing-with-curl)
- [Production Debugging Guide](#-production-level-debugging-guide)
- [Docker Commands](#-docker-commands)
- [Environment Variables](#️-environment-variables)
- [Database Schema](#️-database-schema)
- [Postman Collection](#-postman-collection)
- [Development Workflow](#-development-workflow)
- [Performance Metrics](#-performance-metrics)
- [Future Roadmap](#-future-roadmap)
- [Author](#-author)

---

## 🎬 Quick Demo
```bash
# One command to run everything
git clone https://github.com/YOUR_USERNAME/incident-management-system.git
cd incident-management-system
docker-compose up --build

# Access
# Dashboard: http://localhost:3000
# API Docs:  http://localhost:8081/swagger-ui.html
# Login:     admin / admin123
```

---

## 📌 What is This?

This is a **full-stack incident management system** similar to PagerDuty, ServiceNow, or OpsGenie. It helps IT teams:

| Problem | Solution |
|---------|----------|
| Server goes down at 3 AM | 🔔 Instant notifications via Email, SMS, Slack |
| Who's working on it? | 👤 Incident assignment & ownership |
| Is it fixed yet? | 📊 Real-time status tracking |
| Same issue happening again? | 📈 Incident history & patterns |
| Taking too long to fix? | ⚡ Auto-escalation to managers |

### Incident Lifecycle
```
┌───────────┐     ┌──────────────┐     ┌──────────┐     ┌────────┐
│ TRIGGERED │ --> │ ACKNOWLEDGED │ --> │ RESOLVED │ --> │ CLOSED │
└───────────┘     └──────────────┘     └──────────┘     └────────┘
```

1. **TRIGGERED:** Incident is created (e.g., "Database server down")
2. **ACKNOWLEDGED:** Someone is looking at it
3. **RESOLVED:** Problem is fixed
4. **CLOSED:** Incident is archived

---

## 💡 Why I Built This

I built this project to:

1. **Learn Microservices Architecture:** Understanding how multiple services communicate
2. **Implement Event-Driven Design:** Using Kafka for real-time notifications
3. **Practice Caching Strategies:** Using Redis to improve API performance
4. **Understand Security:** Implementing JWT authentication from scratch
5. **Learn DevOps:** Docker containerization and CI/CD pipelines
6. **Build a Portfolio Project:** Something real-world to show in interviews

This is not just a CRUD application. It demonstrates:
- How modern backend systems are designed
- How different technologies work together
- How to build scalable, production-ready applications

---

## 📚 What I Learned

### 1. Spring Boot & REST APIs
- Building RESTful APIs with proper HTTP methods (GET, POST, PATCH, PUT, DELETE)
- Request validation using Jakarta Validation
- Global exception handling with @ControllerAdvice
- Pagination and sorting for large datasets

### 2. Database & JPA
- Entity relationships and mappings
- Writing custom queries with @Query annotation
- Database migrations and schema design
- Connection pooling with HikariCP

### 3. Security
- JWT (JSON Web Token) authentication
- Role-based access control (RBAC)
- Password encryption with BCrypt
- Securing REST endpoints

### 4. Caching with Redis
- Cache-aside pattern implementation
- Cache invalidation strategies
- Reduced API response time from 50-100ms to 1-2ms
- Understanding when to cache and when not to

### 5. Event-Driven Architecture with Kafka
- Producer-Consumer pattern
- Asynchronous communication between services
- Event serialization/deserialization
- Handling message failures

### 6. Docker & DevOps
- Writing Dockerfiles for Java applications
- Docker Compose for multi-container orchestration
- Environment variable management
- CI/CD with GitHub Actions

### 7. Frontend with React
- Component-based architecture
- State management with hooks
- API integration with Axios
- Responsive UI design

---

## 🛠️ Tech Stack
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              FRONTEND                                       │
│                                                                             │
│   React 18  •  Axios  •  React Router  •  CSS3                             │
│                                                                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                              BACKEND                                        │
│                                                                             │
│   Java 17  •  Spring Boot 3.2  •  Spring Security  •  Spring Data JPA     │
│                                                                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                              DATA LAYER                                     │
│                                                                             │
│   PostgreSQL 15 (Database)  •  Redis 7 (Cache)  •  Kafka (Events)         │
│                                                                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                              DEVOPS                                         │
│                                                                             │
│   Docker  •  Docker Compose  •  GitHub Actions  •  Swagger/OpenAPI        │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

| Layer | Technology | Why I Chose It |
|-------|------------|----------------|
| **Backend** | Java 17, Spring Boot 3.2 | Industry standard, huge ecosystem |
| **Database** | PostgreSQL 15 | ACID compliance, complex queries, JSON support |
| **Cache** | Redis 7 | Sub-millisecond latency, simple API |
| **Message Queue** | Apache Kafka | Durability, replayability, high throughput |
| **Frontend** | React 18 | Component-based, large community, hooks |
| **Auth** | JWT | Stateless, scalable, no server-side sessions |
| **Docs** | Swagger/OpenAPI | Interactive API documentation |
| **Containers** | Docker | Consistency across environments |
| **CI/CD** | GitHub Actions | Free, integrated with GitHub |

---

## 🏗️ System Architecture
```
                                   ┌──────────────────┐
                                   │   React App      │
                                   │   Port 3000      │
                                   └────────┬─────────┘
                                            │
                                            │ REST API
                                            ▼
┌───────────────────────────────────────────────────────────────────────────────┐
│                                                                               │
│                        INCIDENT-SERVICE (Port 8081)                          │
│                                                                               │
│  ┌─────────────┐   ┌─────────────┐   ┌─────────────┐   ┌─────────────────┐  │
│  │    Auth     │   │  Incident   │   │   Redis     │   │     Kafka       │  │
│  │ Controller  │   │ Controller  │   │   Cache     │   │    Producer     │  │
│  │             │   │             │   │             │   │                 │  │
│  │ • Register  │   │ • Create    │   │ • Get/Set   │   │ • Publish       │  │
│  │ • Login     │   │ • Update    │   │ • Evict     │   │   Events        │  │
│  │ • JWT Auth  │   │ • Lifecycle │   │ • 30min TTL │   │                 │  │
│  └─────────────┘   └─────────────┘   └──────┬──────┘   └────────┬────────┘  │
│                                             │                    │           │
└─────────────────────────────────────────────┼────────────────────┼───────────┘
                                              │                    │
              ┌───────────────────────────────┤                    │
              │                               │                    │
              ▼                               ▼                    ▼
     ┌─────────────────┐            ┌─────────────────┐   ┌─────────────────┐
     │   PostgreSQL    │            │      Redis      │   │      Kafka      │
     │   Port 5432     │            │   Port 6379     │   │   Port 9092     │
     │                 │            │                 │   │                 │
     │ • Users         │            │ • Incident      │   │ • incident-     │
     │ • Incidents     │            │   Cache         │   │   events topic  │
     │ • Audit Trail   │            │ • Fast Reads    │   │                 │
     └─────────────────┘            └─────────────────┘   └────────┬────────┘
                                                                   │
                                                                   │ Consume
                                                                   ▼
                                    ┌───────────────────────────────────────────┐
                                    │                                           │
                                    │     NOTIFICATION-SERVICE (Port 8082)     │
                                    │                                           │
                                    │  ┌───────────┐ ┌───────┐ ┌─────────────┐ │
                                    │  │   Email   │ │  SMS  │ │    Slack    │ │
                                    │  │  Service  │ │Service│ │   Service   │ │
                                    │  └───────────┘ └───────┘ └─────────────┘ │
                                    │                                           │
                                    └───────────────────────────────────────────┘
```

### How It Works

1. **User** accesses React Dashboard or Swagger UI
2. **React/Swagger** sends HTTP requests to incident-service
3. **incident-service** handles business logic:
   - Authenticates user via JWT
   - Checks Redis cache first (fast!)
   - If not in cache, queries PostgreSQL
   - Publishes events to Kafka
4. **notification-service** consumes Kafka events
5. **Notifications** are sent via Email, SMS, Slack

---

## 📁 Project Structure
```
incident-management-system/
│
├── 📂 incident-service/                 # Main Backend Service (Spring Boot)
│   ├── 📂 src/main/java/com/incident/incidentservice/
│   │   ├── 📂 config/                   # Configuration classes
│   │   │   ├── RedisConfig.java         # Redis cache configuration
│   │   │   ├── SecurityConfig.java      # Spring Security + JWT config
│   │   │   └── SwaggerConfig.java       # OpenAPI documentation
│   │   │
│   │   ├── 📂 controller/               # REST API endpoints
│   │   │   ├── AuthController.java      # /api/auth/* endpoints
│   │   │   └── IncidentController.java  # /api/incidents/* endpoints
│   │   │
│   │   ├── 📂 service/                  # Business logic layer
│   │   │   ├── AuthService.java         # Registration, login, JWT
│   │   │   ├── IncidentService.java     # Incident CRUD + lifecycle
│   │   │   └── IncidentCacheService.java # Redis caching logic
│   │   │
│   │   ├── 📂 repository/               # Data access layer
│   │   │   ├── UserRepository.java      # User database queries
│   │   │   └── IncidentRepository.java  # Incident database queries
│   │   │
│   │   ├── 📂 entity/                   # JPA entities (database tables)
│   │   │   ├── User.java                # Users table
│   │   │   └── Incident.java            # Incidents table
│   │   │
│   │   ├── 📂 dto/                      # Data Transfer Objects
│   │   │   ├── CreateIncidentRequest.java
│   │   │   ├── UpdateIncidentRequest.java
│   │   │   ├── IncidentResponse.java
│   │   │   ├── ApiResponse.java         # Standard API response wrapper
│   │   │   └── 📂 auth/
│   │   │       ├── LoginRequest.java
│   │   │       ├── RegisterRequest.java
│   │   │       └── AuthResponse.java
│   │   │
│   │   ├── 📂 security/                 # JWT Authentication
│   │   │   ├── JwtService.java          # Token generation & validation
│   │   │   ├── JwtAuthenticationFilter.java # Request filter
│   │   │   └── SecurityConfig.java      # Security rules
│   │   │
│   │   ├── 📂 kafka/                    # Event producer
│   │   │   ├── IncidentEventProducer.java # Sends events to Kafka
│   │   │   └── IncidentEvent.java       # Event data structure
│   │   │
│   │   ├── 📂 exception/                # Error handling
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── IncidentNotFoundException.java
│   │   │   └── InvalidStatusTransitionException.java
│   │   │
│   │   ├── 📂 enums/                    # Enumerations
│   │   │   ├── Severity.java            # P1, P2, P3, P4
│   │   │   ├── IncidentStatus.java      # TRIGGERED, ACKNOWLEDGED, etc.
│   │   │   └── Role.java                # ADMIN, USER, VIEWER
│   │   │
│   │   └── 📂 mapper/                   # Entity-DTO conversion
│   │       └── IncidentMapper.java
│   │
│   ├── 📂 src/main/resources/
│   │   └── application.yml              # App configuration
│   │
│   ├── 📂 src/test/java/                # Unit & integration tests
│   ├── 📄 Dockerfile                    # Container build instructions
│   └── 📄 pom.xml                       # Maven dependencies
│
├── 📂 notification-service/             # Kafka Consumer Service
│   ├── 📂 src/main/java/com/incident/notification/
│   │   ├── 📂 kafka/
│   │   │   ├── IncidentEventConsumer.java # Listens to Kafka events
│   │   │   └── IncidentEvent.java       # Event data structure
│   │   │
│   │   └── 📂 service/                  # Notification senders
│   │       ├── EmailService.java        # Email notifications
│   │       ├── SmsService.java          # SMS notifications
│   │       └── SlackService.java        # Slack notifications
│   │
│   ├── 📂 src/main/resources/
│   │   └── application.yml
│   ├── 📄 Dockerfile
│   └── 📄 pom.xml
│
├── 📂 incident-dashboard/               # React Frontend
│   ├── 📂 src/
│   │   ├── 📂 components/               # UI components
│   │   │   ├── Login.js                 # Login page
│   │   │   ├── Dashboard.js             # Main dashboard
│   │   │   ├── IncidentList.js          # Incidents table
│   │   │   ├── IncidentForm.js          # Create/edit form
│   │   │   └── IncidentDetails.js       # Single incident view
│   │   │
│   │   ├── 📂 services/                 # API calls
│   │   │   ├── api.js                   # Axios instance
│   │   │   ├── authService.js           # Auth API calls
│   │   │   └── incidentService.js       # Incident API calls
│   │   │
│   │   ├── App.js                       # Main component
│   │   ├── App.css                      # Styles
│   │   └── index.js                     # Entry point
│   │
│   ├── 📄 package.json                  # NPM dependencies
│   └── 📄 Dockerfile
│
├── 📂 .github/workflows/                # CI/CD Pipeline
│   └── 📄 build.yml                     # GitHub Actions workflow
│
├── 📄 docker-compose.yml                # Multi-container orchestration
├── 📄 README.md                         # This file
└── 📄 LICENSE                           # MIT License
```

### Key Files Explained

| File | Purpose |
|------|---------|
| `IncidentController.java` | All incident REST endpoints |
| `IncidentService.java` | Business logic for incidents |
| `IncidentCacheService.java` | Redis caching implementation |
| `JwtService.java` | JWT token creation & validation |
| `IncidentEventProducer.java` | Sends events to Kafka |
| `IncidentEventConsumer.java` | Receives events from Kafka |
| `SecurityConfig.java` | Which URLs need authentication |
| `docker-compose.yml` | Starts all services together |
| `build.yml` | CI/CD pipeline definition |

---

## ✨ Features

### 🔐 Authentication & Security
```
✅ JWT-based stateless authentication
✅ Role-based access control (ADMIN, USER, VIEWER)
✅ BCrypt password encryption
✅ Protected API endpoints
✅ Token expiration & refresh
```

### 🎫 Incident Management
```
✅ Create incidents with severity (P1-P4)
✅ Status lifecycle: TRIGGERED → ACKNOWLEDGED → RESOLVED → CLOSED
✅ Assign incidents to team members
✅ Escalate unresolved incidents
✅ Filter by status, severity, assignee
✅ Pagination & sorting
```

### 🔔 Real-Time Notifications
```
✅ Email notifications (simulated)
✅ SMS notifications (simulated)
✅ Slack notifications (simulated)
✅ Event-driven via Apache Kafka
✅ Async processing (non-blocking)
```

### ⚡ Performance
```
✅ Redis caching (1-2ms response)
✅ Cache invalidation on updates
✅ Database connection pooling
✅ Optimized JPA queries
```

### 🐳 DevOps
```
✅ Docker containerization
✅ Docker Compose orchestration
✅ GitHub Actions CI/CD
✅ Health check endpoints
✅ Swagger API documentation
```

---

## 🚀 Getting Started

### Prerequisites

| Software | Version | Required |
|----------|---------|----------|
| Docker Desktop | Latest | ✅ Yes |
| Git | Latest | ✅ Yes |
| Java 17 | 17+ | ⚪ For local dev only |
| Node.js | 18+ | ⚪ For local dev only |
| Maven | 3.9+ | ⚪ For local dev only |

### 🐳 Run with Docker (Recommended)
```bash
# Clone
git clone https://github.com/YOUR_USERNAME/incident-management-system.git
cd incident-management-system

# Start all services (takes 3-5 minutes first time)
docker-compose up --build

# Verify all services are running
docker-compose ps
```

**Services Started:**

| Service | Port | URL |
|---------|------|-----|
| React Dashboard | 3000 | http://localhost:3000 |
| incident-service | 8081 | http://localhost:8081 |
| notification-service | 8082 | http://localhost:8082 |
| Swagger UI | 8081 | http://localhost:8081/swagger-ui.html |
| PostgreSQL | 5432 | - |
| Redis | 6379 | - |
| Kafka | 9092 | - |

### 💻 Run Locally (For Development)

<details>
<summary>Click to expand local setup instructions</summary>

**Terminal 1 - Redis:**
```bash
redis-server
```

**Terminal 2 - Kafka:**
```bash
cd C:\kafka
.\bin\windows\kafka-server-start.bat .\config\kraft\server.properties
```

**Terminal 3 - incident-service:**
```bash
cd incident-service
mvn spring-boot:run
```

**Terminal 4 - notification-service:**
```bash
cd notification-service
mvn spring-boot:run
```

**Terminal 5 - React:**
```bash
cd incident-dashboard
npm install
npm start
```

</details>

---

## 📖 API Reference

### Base URL
```
http://localhost:8081/api
```

### 🔑 Authentication Endpoints

<details>
<summary><code>POST</code> <code>/api/auth/register</code> - Register new user</summary>

**Request:**
```json
{
    "username": "admin",
    "email": "admin@company.com",
    "password": "admin123",
    "role": "ADMIN"
}
```

**Response:** `200 OK`
```json
{
    "success": true,
    "data": {
        "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiI...",
        "username": "admin",
        "email": "admin@company.com",
        "role": "ADMIN"
    }
}
```

**Roles Available:** `ADMIN`, `USER`, `VIEWER`

</details>

<details>
<summary><code>POST</code> <code>/api/auth/login</code> - Login</summary>

**Request:**
```json
{
    "username": "admin",
    "password": "admin123"
}
```

**Response:** `200 OK`
```json
{
    "success": true,
    "data": {
        "token": "eyJhbGciOiJIUzM4NCJ9...",
        "username": "admin",
        "email": "admin@company.com",
        "role": "ADMIN"
    }
}
```

</details>

### 🎫 Incident Endpoints

> **Note:** All incident endpoints require `Authorization: Bearer <token>` header

<details>
<summary><code>POST</code> <code>/api/incidents</code> - Create incident</summary>

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request:**
```json
{
    "title": "Database server down",
    "description": "Production DB not responding",
    "severity": "P1"
}
```

**Severity Levels:**

| Level | Description | Response Time |
|-------|-------------|---------------|
| P1 | Critical | 15 minutes |
| P2 | High | 1 hour |
| P3 | Medium | 4 hours |
| P4 | Low | 24 hours |

**Response:** `201 Created`
```json
{
    "success": true,
    "data": {
        "id": 1,
        "incidentNumber": "INC-20260113-0001",
        "title": "Database server down",
        "severity": "P1",
        "status": "TRIGGERED",
        "createdAt": "2026-01-13T10:30:00Z"
    },
    "message": "Incident created"
}
```

</details>

<details>
<summary><code>GET</code> <code>/api/incidents</code> - Get all incidents</summary>

**Query Parameters:**

| Parameter | Default | Description |
|-----------|---------|-------------|
| page | 0 | Page number |
| size | 20 | Items per page |
| sortBy | createdAt | Sort field |
| sortDir | desc | Sort direction |

**Response:** `200 OK`
```json
{
    "success": true,
    "data": {
        "content": [...],
        "page": 0,
        "size": 20,
        "totalElements": 100,
        "totalPages": 5
    }
}
```

</details>

<details>
<summary><code>GET</code> <code>/api/incidents/{id}</code> - Get incident by ID</summary>

**Response:** `200 OK`
```json
{
    "success": true,
    "data": {
        "id": 1,
        "incidentNumber": "INC-20260113-0001",
        "title": "Database server down",
        "severity": "P1",
        "status": "TRIGGERED",
        "assigneeId": null,
        "escalationLevel": 0,
        "createdAt": "2026-01-13T10:30:00Z"
    }
}
```

</details>

<details>
<summary><code>GET</code> <code>/api/incidents/active</code> - Get active incidents</summary>

**Response:** `200 OK`
```json
{
    "success": true,
    "data": [
        {
            "id": 1,
            "incidentNumber": "INC-20260113-0001",
            "title": "Database server down",
            "status": "TRIGGERED"
        }
    ]
}
```

</details>

<details>
<summary><code>GET</code> <code>/api/incidents/filter</code> - Filter incidents</summary>

**Query Parameters:**

| Parameter | Required | Description |
|-----------|----------|-------------|
| status | No | TRIGGERED, ACKNOWLEDGED, RESOLVED, CLOSED |
| severity | No | P1, P2, P3, P4 |
| assigneeId | No | User ID |

**Example:** `/api/incidents/filter?status=TRIGGERED&severity=P1`

</details>

<details>
<summary><code>PUT</code> <code>/api/incidents/{id}</code> - Update incident</summary>

**Request:**
```json
{
    "title": "Updated title",
    "description": "Updated description",
    "severity": "P2"
}
```

</details>

<details>
<summary><code>PATCH</code> <code>/api/incidents/{id}/acknowledge</code> - Acknowledge</summary>

**Response:** `200 OK`
```json
{
    "success": true,
    "data": {
        "id": 1,
        "status": "ACKNOWLEDGED",
        "acknowledgedAt": "2026-01-13T10:35:00Z"
    },
    "message": "Incident acknowledged"
}
```

</details>

<details>
<summary><code>PATCH</code> <code>/api/incidents/{id}/resolve</code> - Resolve</summary>

**Query:** `?resolution=Fixed the database connection`

**Response:** `200 OK`
```json
{
    "success": true,
    "data": {
        "id": 1,
        "status": "RESOLVED",
        "resolvedAt": "2026-01-13T11:00:00Z"
    },
    "message": "Incident resolved"
}
```

</details>

<details>
<summary><code>PATCH</code> <code>/api/incidents/{id}/close</code> - Close</summary>

**Response:** `200 OK`
```json
{
    "success": true,
    "data": {
        "id": 1,
        "status": "CLOSED",
        "closedAt": "2026-01-13T11:30:00Z"
    },
    "message": "Incident closed"
}
```

</details>

<details>
<summary><code>PATCH</code> <code>/api/incidents/{id}/escalate</code> - Escalate</summary>

**Response:** `200 OK`
```json
{
    "success": true,
    "data": {
        "id": 1,
        "escalationLevel": 1,
        "status": "ESCALATED"
    },
    "message": "Incident escalated"
}
```

</details>

<details>
<summary><code>PATCH</code> <code>/api/incidents/{id}/assign</code> - Assign</summary>

**Query:** `?assigneeId=2`

**Response:** `200 OK`
```json
{
    "success": true,
    "data": {
        "id": 1,
        "assigneeId": 2,
        "assigneeName": "john.doe"
    },
    "message": "Incident assigned"
}
```

</details>

---

## 🧪 Testing with Swagger UI

### Step 1: Open Swagger
```
http://localhost:8081/swagger-ui.html
```

### Step 2: Register User
1. Expand **Authentication** section
2. Click **POST /api/auth/register**
3. Click **Try it out**
4. Enter:
```json
{
    "username": "admin",
    "email": "admin@test.com",
    "password": "admin123",
    "role": "ADMIN"
}
```
5. Click **Execute**
6. **Copy the token** from response

### Step 3: Authorize
1. Click **Authorize** button (top right 🔒)
2. Enter: `Bearer <paste-your-token-here>`
3. Click **Authorize**
4. Click **Close**

### Step 4: Test APIs
Now you can test any endpoint - the token is automatically included!

---

## 🔧 Testing with Postman

### Setup Environment Variables

Create these variables in Postman:

| Variable | Value |
|----------|-------|
| `base_url` | `http://localhost:8081` |
| `token` | (leave empty, set after login) |

### API Requests

<details>
<summary><strong>1. Register User</strong></summary>
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

</details>

<details>
<summary><strong>2. Login</strong></summary>
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

**Auto-save token - Add to Tests tab:**
```javascript
var jsonData = pm.response.json();
if (jsonData.success) {
    pm.collectionVariables.set("token", jsonData.data.token);
}
```

</details>

<details>
<summary><strong>3. Create Incident</strong></summary>
```
Method: POST
URL: {{base_url}}/api/incidents
Headers:
    Content-Type: application/json
    Authorization: Bearer {{token}}
Body (raw JSON):
{
    "title": "Production Database Down",
    "description": "MySQL server not responding",
    "severity": "P1"
}
```

</details>

<details>
<summary><strong>4. Get All Incidents</strong></summary>
```
Method: GET
URL: {{base_url}}/api/incidents?page=0&size=10
Headers:
    Authorization: Bearer {{token}}
```

</details>

<details>
<summary><strong>5. Get Incident by ID</strong></summary>
```
Method: GET
URL: {{base_url}}/api/incidents/1
Headers:
    Authorization: Bearer {{token}}
```

</details>

<details>
<summary><strong>6. Acknowledge Incident</strong></summary>
```
Method: PATCH
URL: {{base_url}}/api/incidents/1/acknowledge
Headers:
    Authorization: Bearer {{token}}
```

</details>

<details>
<summary><strong>7. Resolve Incident</strong></summary>
```
Method: PATCH
URL: {{base_url}}/api/incidents/1/resolve?resolution=Fixed%20the%20issue
Headers:
    Authorization: Bearer {{token}}
```

</details>

<details>
<summary><strong>8. Close Incident</strong></summary>
```
Method: PATCH
URL: {{base_url}}/api/incidents/1/close
Headers:
    Authorization: Bearer {{token}}
```

</details>

<details>
<summary><strong>9. Escalate Incident</strong></summary>
```
Method: PATCH
URL: {{base_url}}/api/incidents/1/escalate
Headers:
    Authorization: Bearer {{token}}
```

</details>

<details>
<summary><strong>10. Assign Incident</strong></summary>
```
Method: PATCH
URL: {{base_url}}/api/incidents/1/assign?assigneeId=2
Headers:
    Authorization: Bearer {{token}}
```

</details>

---

## 💻 Testing with cURL
```bash
# 1. Register
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@test.com","password":"admin123","role":"ADMIN"}'

# 2. Login (copy token from response)
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 3. Create Incident (replace YOUR_TOKEN)
curl -X POST http://localhost:8081/api/incidents \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"title":"Server Down","severity":"P1"}'

# 4. Get All Incidents
curl -X GET http://localhost:8081/api/incidents \
  -H "Authorization: Bearer YOUR_TOKEN"

# 5. Acknowledge
curl -X PATCH http://localhost:8081/api/incidents/1/acknowledge \
  -H "Authorization: Bearer YOUR_TOKEN"

# 6. Resolve
curl -X PATCH "http://localhost:8081/api/incidents/1/resolve?resolution=Fixed" \
  -H "Authorization: Bearer YOUR_TOKEN"

# 7. Close
curl -X PATCH http://localhost:8081/api/incidents/1/close \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Verify Kafka Notifications
```bash
docker-compose logs notification-service --tail=50

# Expected output:
# 📥 Received event: CREATED for incident: INC-20260113-0001
# 📧 EMAIL SENT - To: oncall-team@company.com
# 📱 SMS SENT - To: +91-9999999999  
# 💬 SLACK MESSAGE SENT - Channel: #incidents
```

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

### Common Errors & Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| 401 Unauthorized | Missing token | Add `Authorization: Bearer <token>` |
| 401 Unauthorized | Token expired | Login again to get new token |
| 403 Forbidden | Wrong role | Use account with correct role |
| 404 Not Found | Wrong ID | Check if resource exists |
| 500 Internal Error | Check logs | `docker-compose logs incident-service` |

### Debugging Checklist
```bash
# 1. Check all services running
docker-compose ps

# 2. Check application logs
docker-compose logs incident-service --tail=100

# 3. Check health endpoint
curl http://localhost:8081/actuator/health

# 4. Check database connection
docker exec -it incident-postgres psql -U postgres -c "SELECT 1"

# 5. Check Redis connection
docker exec -it incident-redis redis-cli PING

# 6. Check Kafka topics
docker exec incident-kafka kafka-topics.sh --list --bootstrap-server localhost:9092
```

### Real-World Debugging Scenarios

<details>
<summary><strong>Scenario 1: API Returns 500 Error</strong></summary>

**Debug Steps:**
```bash
# 1. Check logs
docker-compose logs incident-service --tail=50

# 2. Look for exception
# Found: "Connection refused: localhost:5432"

# 3. Fix
docker-compose restart postgres
```

</details>

<details>
<summary><strong>Scenario 2: Notifications Not Sending</strong></summary>

**Debug Steps:**
```bash
# 1. Check notification-service logs
docker-compose logs notification-service --tail=50

# 2. Check Kafka
docker exec incident-kafka kafka-topics.sh --list --bootstrap-server localhost:9092

# 3. Fix
docker-compose restart kafka
```

</details>

<details>
<summary><strong>Scenario 3: Slow API Response</strong></summary>

**Debug Steps:**
```bash
# 1. Check Redis cache
docker exec incident-redis redis-cli KEYS "*"

# 2. If empty, cache was cleared
# First request will be slow, second should be fast
```

</details>

---

## 🐳 Docker Commands
```bash
# Start
docker-compose up --build          # Build & start
docker-compose up -d               # Start in background

# Stop
docker-compose down                # Stop all
docker-compose down -v             # Stop & remove volumes

# Logs
docker-compose logs                # All logs
docker-compose logs -f             # Follow logs
docker-compose logs incident-service --tail=50

# Debug
docker-compose ps                  # List services
docker exec -it incident-service sh  # Shell into container

# Clean
docker system prune -f             # Remove unused data
```

---

## ⚙️ Environment Variables

### incident-service

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | PostgreSQL host | `localhost` |
| `DB_PORT` | PostgreSQL port | `5432` |
| `DB_NAME` | Database name | `incident_db` |
| `DB_USER` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |
| `REDIS_HOST` | Redis host | `localhost` |
| `REDIS_PORT` | Redis port | `6379` |
| `KAFKA_SERVERS` | Kafka bootstrap servers | `localhost:9092` |
| `JWT_SECRET` | Secret key for JWT signing | (configured in app) |
| `JWT_EXPIRATION` | Token expiration in ms | `86400000` (24 hours) |

### notification-service

| Variable | Description | Default |
|----------|-------------|---------|
| `KAFKA_SERVERS` | Kafka bootstrap servers | `localhost:9092` |
| `KAFKA_GROUP_ID` | Consumer group ID | `notification-group` |

### Docker Environment

All environment variables are pre-configured in `docker-compose.yml`. No changes needed for local development.

**For production, set these securely:**
```bash
export DB_PASSWORD=your_secure_password
export JWT_SECRET=your_very_long_secret_key_at_least_32_characters
```

---

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50) UNIQUE NOT NULL,
    email           VARCHAR(100) UNIQUE NOT NULL,
    password        VARCHAR(255) NOT NULL,
    role            VARCHAR(20) NOT NULL,
    team_id         VARCHAR(50),
    team_name       VARCHAR(100),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| username | VARCHAR(50) | Unique username |
| email | VARCHAR(100) | User email |
| password | VARCHAR(255) | BCrypt encrypted |
| role | VARCHAR(20) | ADMIN, USER, VIEWER |

### Incidents Table
```sql
CREATE TABLE incidents (
    id                  BIGSERIAL PRIMARY KEY,
    incident_number     VARCHAR(50) UNIQUE NOT NULL,
    title               VARCHAR(255) NOT NULL,
    description         TEXT,
    severity            VARCHAR(10) NOT NULL,
    status              VARCHAR(20) NOT NULL,
    assignee_id         BIGINT REFERENCES users(id),
    assignee_name       VARCHAR(100),
    team_id             VARCHAR(50),
    team_name           VARCHAR(100),
    escalation_level    INTEGER DEFAULT 0,
    sla_breach          BOOLEAN DEFAULT FALSE,
    resolution          TEXT,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    acknowledged_at     TIMESTAMP,
    resolved_at         TIMESTAMP,
    closed_at           TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| incident_number | VARCHAR(50) | Unique ID (INC-20260113-0001) |
| title | VARCHAR(255) | Incident title |
| description | TEXT | Detailed description |
| severity | VARCHAR(10) | P1, P2, P3, P4 |
| status | VARCHAR(20) | TRIGGERED, ACKNOWLEDGED, RESOLVED, CLOSED |
| assignee_id | BIGINT | FK to users table |
| escalation_level | INTEGER | 0, 1, 2, 3... |
| sla_breach | BOOLEAN | SLA violated? |
| resolution | TEXT | How it was fixed |

### Entity Relationship Diagram
```
┌─────────────────┐         ┌─────────────────────┐
│     USERS       │         │     INCIDENTS       │
├─────────────────┤         ├─────────────────────┤
│ id (PK)         │────┐    │ id (PK)             │
│ username        │    │    │ incident_number     │
│ email           │    │    │ title               │
│ password        │    │    │ description         │
│ role            │    │    │ severity            │
│ team_id         │    │    │ status              │
│ team_name       │    └───>│ assignee_id (FK)    │
│ created_at      │         │ assignee_name       │
│ updated_at      │         │ escalation_level    │
└─────────────────┘         │ sla_breach          │
                            │ resolution          │
                            │ created_at          │
                            │ acknowledged_at     │
                            │ resolved_at         │
                            │ closed_at           │
                            └─────────────────────┘
```

---

## 📬 Postman Collection

### Quick Import

1. Open Postman
2. Click **Import**
3. Select **Raw Text**
4. Paste the JSON below
5. Click **Import**

<details>
<summary><strong>Click to expand Postman Collection JSON</strong></summary>
```json
{
    "info": {
        "name": "Incident Management System",
        "_postman_id": "incident-mgmt-collection",
        "description": "Complete API collection for Incident Management System",
        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
    },
    "variable": [
        {
            "key": "base_url",
            "value": "http://localhost:8081"
        },
        {
            "key": "token",
            "value": ""
        }
    ],
    "item": [
        {
            "name": "Authentication",
            "item": [
                {
                    "name": "Register User",
                    "request": {
                        "method": "POST",
                        "header": [
                            {"key": "Content-Type", "value": "application/json"}
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
                    },
                    "event": [
                        {
                            "listen": "test",
                            "script": {
                                "exec": [
                                    "var jsonData = pm.response.json();",
                                    "if (jsonData.success && jsonData.data.token) {",
                                    "    pm.collectionVariables.set('token', jsonData.data.token);",
                                    "}"
                                ],
                                "type": "text/javascript"
                            }
                        }
                    ]
                },
                {
                    "name": "Login",
                    "request": {
                        "method": "POST",
                        "header": [
                            {"key": "Content-Type", "value": "application/json"}
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
                    },
                    "event": [
                        {
                            "listen": "test",
                            "script": {
                                "exec": [
                                    "var jsonData = pm.response.json();",
                                    "if (jsonData.success && jsonData.data.token) {",
                                    "    pm.collectionVariables.set('token', jsonData.data.token);",
                                    "}"
                                ],
                                "type": "text/javascript"
                            }
                        }
                    ]
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
                            {"key": "Content-Type", "value": "application/json"},
                            {"key": "Authorization", "value": "Bearer {{token}}"}
                        ],
                        "body": {
                            "mode": "raw",
                            "raw": "{\n    \"title\": \"Database Server Down\",\n    \"description\": \"Production database not responding\",\n    \"severity\": \"P1\"\n}"
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
                            {"key": "Authorization", "value": "Bearer {{token}}"}
                        ],
                        "url": {
                            "raw": "{{base_url}}/api/incidents?page=0&size=20",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents"],
                            "query": [
                                {"key": "page", "value": "0"},
                                {"key": "size", "value": "20"}
                            ]
                        }
                    }
                },
                {
                    "name": "Get Incident by ID",
                    "request": {
                        "method": "GET",
                        "header": [
                            {"key": "Authorization", "value": "Bearer {{token}}"}
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
                            {"key": "Authorization", "value": "Bearer {{token}}"}
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
                            {"key": "Authorization", "value": "Bearer {{token}}"}
                        ],
                        "url": {
                            "raw": "{{base_url}}/api/incidents/1/resolve?resolution=Fixed the issue",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents", "1", "resolve"],
                            "query": [
                                {"key": "resolution", "value": "Fixed the issue"}
                            ]
                        }
                    }
                },
                {
                    "name": "Close Incident",
                    "request": {
                        "method": "PATCH",
                        "header": [
                            {"key": "Authorization", "value": "Bearer {{token}}"}
                        ],
                        "url": {
                            "raw": "{{base_url}}/api/incidents/1/close",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents", "1", "close"]
                        }
                    }
                },
                {
                    "name": "Escalate Incident",
                    "request": {
                        "method": "PATCH",
                        "header": [
                            {"key": "Authorization", "value": "Bearer {{token}}"}
                        ],
                        "url": {
                            "raw": "{{base_url}}/api/incidents/1/escalate",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents", "1", "escalate"]
                        }
                    }
                },
                {
                    "name": "Assign Incident",
                    "request": {
                        "method": "PATCH",
                        "header": [
                            {"key": "Authorization", "value": "Bearer {{token}}"}
                        ],
                        "url": {
                            "raw": "{{base_url}}/api/incidents/1/assign?assigneeId=2",
                            "host": ["{{base_url}}"],
                            "path": ["api", "incidents", "1", "assign"],
                            "query": [
                                {"key": "assigneeId", "value": "2"}
                            ]
                        }
                    }
                }
            ]
        },
        {
            "name": "Health Check",
            "item": [
                {
                    "name": "Health",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/actuator/health",
                            "host": ["{{base_url}}"],
                            "path": ["actuator", "health"]
                        }
                    }
                }
            ]
        }
    ]
}
```

</details>

### How to Use

1. **Import** the collection into Postman
2. **Run Register** or **Login** first (token auto-saves)
3. **Test any endpoint** - token is automatically included

---

## 🔄 Development Workflow
```bash
# After code changes:

# 1. Build JARs
cd incident-service && mvn clean package -DskipTests && cd ..
cd notification-service && mvn clean package -DskipTests && cd ..

# 2. Push to GitHub
git add . && git commit -m "Your changes" && git push

# 3. Rebuild Docker
docker-compose down && docker-compose up --build
```

---

## 📊 Performance Metrics

| Metric | Without Cache | With Redis Cache | Improvement |
|--------|---------------|------------------|-------------|
| GET /incidents/{id} | 50-100ms | 1-2ms | **50x faster** |
| Database queries | Every request | First request only | **Reduced load** |

---

## 🔮 Future Roadmap

### Phase 2 - Enhanced Features
- [ ] Real Twilio SMS integration
- [ ] Real SendGrid email integration  
- [ ] Real Slack API integration
- [ ] Incident comments & attachments

### Phase 3 - Scalability
- [ ] Kubernetes deployment
- [ ] Database read replicas
- [ ] Redis cluster

### Phase 4 - Observability
- [ ] Prometheus metrics
- [ ] Grafana dashboards
- [ ] ELK stack logging

---

## 👨‍💻 Author

**Rushikesh**

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/YOUR_USERNAME)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/YOUR_LINKEDIN)

---

## 📄 License

Distributed under the MIT License.

---

## ⭐ Support

If this project helped you, please give it a ⭐!

---

<p align="center">Made with ❤️ and ☕</p>
