# FlexiDrive | Milestone 5: Extended Microservices Architecture

A distributed vehicle rental platform built with Spring Boot microservices, RabbitMQ message queue, and automated CI/CD deployment.

---

## Team Members
- Râpa Denis - Andrei
- Bațagoi Ana - Maria
- Mazilu Stefan

---

## Project Overview

FlexiDrive is a distributed vehicle rental platform built on three autonomous microservices. **Milestone 5** extends the original architecture with:

- 🔄 **RabbitMQ Message Queue** for asynchronous event-driven communication
- 🚀 **GitHub Actions CI/CD Pipeline** for automated build, test, and deployment
- 📊 **Enhanced observability** with RabbitMQ Management UI
- 🛡️ **Improved fault tolerance** and service decoupling

---

## System Architecture

### Core Microservices

The platform consists of three Spring Boot services, each with isolated data persistence:

**1. User Service** (Port 8081)
- Manages user authentication and profiles
- Handles user registration and retrieval
- Foundation for future RBAC implementation

**2. Fleet Service** (Port 8082)
- Manages vehicle inventory and availability
- Maintains real-time vehicle status
- Processes booking events asynchronously via RabbitMQ

**3. Booking Service** (Port 8083)
- Orchestrates booking transactions
- Validates users and vehicle availability
- Publishes events to RabbitMQ for async processing
- Coordinates state changes across services

### Communication Patterns

**Synchronous (REST):**
- User/Vehicle validation before booking
- Immediate consistency for critical operations

**Asynchronous (RabbitMQ):**
- Event publishing for booking lifecycle
- Decoupled service communication
- Message durability and fault tolerance

---

## Technology Stack

- **Runtime:** Java 17 (Amazon Corretto)
- **Framework:** Spring Boot 4.0
- **Message Broker:** RabbitMQ 3 (with Management UI)
- **Database:** H2 (In-Memory)
- **Containerization:** Docker & Docker Compose
- **CI/CD:** GitHub Actions
- **Transport:** REST / JSON + AMQP

---

## Message Queue Integration (RabbitMQ)

### Architecture Overview

FlexiDrive now implements **event-driven architecture** using RabbitMQ as a message broker. This provides asynchronous communication between services, improving system resilience and scalability.

### Message Flow Design

```
┌─────────────────┐      Synchronous REST      ┌──────────────┐
│ Booking Service │ ─────────────────────────> │ Fleet Service│
│                 │   (Immediate validation)    │              │
└────────┬────────┘                             └──────┬───────┘
         │                                             │
         │ Async Event                        Async   │
         │ Publishing                         Listener│
         ▼                                             ▼
    ┌────────────────────────────────────────────────────┐
    │              RabbitMQ Message Broker               │
    │  Exchange: flexidrive.booking.exchange             │
    │  Queue: flexidrive.booking.queue                   │
    │  Routing Key: booking.event                        │
    └────────────────────────────────────────────────────┘
```

### Event Types

1. **BOOKING_CREATED**: Published when a new booking is confirmed
   - Vehicle availability is set to `false` (both sync and async)
   - Event contains: bookingId, userId, vehicleId, dates, timestamp

2. **BOOKING_CANCELLED**: Published when a booking is cancelled
   - Vehicle availability is set to `true` (both sync and async)
   - Enables fleet recovery after cancellations

### Why Both Sync and Async?

**Hybrid Approach Benefits:**
- **Synchronous REST**: Ensures immediate consistency for critical operations
- **Asynchronous RabbitMQ**: Provides durability, decoupling, and future extensibility

### Demonstrated Benefits

#### 1. **Fault Tolerance**
If Fleet Service goes down:
- Booking Service continues to accept reservations
- Events queue up in RabbitMQ
- When Fleet Service recovers, it processes all pending events
- **Zero data loss**

#### 2. **Decoupling**
- Services don't need direct knowledge of each other's state
- New consumers (e.g., NotificationService, AnalyticsService) can subscribe without modifying BookingService
- Change one service without affecting others

#### 3. **Scalability**
- Multiple Fleet Service instances can consume from the same queue (load balancing)
- RabbitMQ handles message distribution
- System can scale horizontally

#### 4. **Observability**
Access RabbitMQ Management UI at **http://localhost:15672** (guest/guest):
- Monitor queue depth
- Track message rates
- View active consumers
- Inspect message payloads

### Testing Message Queue

1. Start the system: `docker-compose up --build`
2. Open RabbitMQ Management: http://localhost:15672
3. Create a booking via Postman
4. Observe in RabbitMQ UI:
   - Queue `flexidrive.booking.queue` receives message
   - Fleet Service consumes it
   - Check logs for "Published BOOKING_CREATED event"

---

## CI/CD Pipeline (GitHub Actions)

### Pipeline Architecture

The automated CI/CD pipeline consists of three sequential jobs:

```
┌──────────────────┐      ┌──────────────────┐      ┌──────────────────┐
│ Build & Test     │ ───> │ Build Docker     │ ───> │ Deploy to Local  │
│ - Compile JAR    │      │ - Create Images  │      │ - docker-compose │
│ - Run Unit Tests │      │ - Tag Images     │      │ - Health Checks  │
└──────────────────┘      └──────────────────┘      └──────────────────┘
```

### Workflow Triggers

- **Push** to `main`, `master`, or `develop` branches
- **Pull Requests** to `main` or `master`

### Job Details

#### Job 1: Build and Test Services
- Checks out code
- Sets up JDK 17
- Builds all three services using Maven
- Runs unit tests
- Uploads JAR artifacts for deployment

#### Job 2: Build Docker Images
- Creates Docker images for all services
- Tags images as `latest`
- Verifies image creation

#### Job 3: Deploy to Docker Compose
- Deploys entire stack (including RabbitMQ)
- Waits for services to initialize
- Performs health checks on all endpoints
- Verifies RabbitMQ management UI
- Displays container logs for debugging
- Cleans up resources

### Running the CI/CD Pipeline

#### Automatic Execution
Simply push code to the repository:
```bash
git add .
git commit -m "Add Milestone 5 features"
git push origin main
```

#### Manual Trigger
1. Go to your GitHub repository
2. Navigate to **Actions** tab
3. Select **FlexiDrive CI/CD Pipeline**
4. Click **Run workflow**

#### Viewing Results
- **GitHub Actions Tab**: See real-time build logs
- **Green checkmark**: All stages passed
- **Red X**: Click for detailed error logs

### Local Testing (Without GitHub)

You can simulate the CI/CD steps locally:

```bash
# Step 1: Build and Test
cd userservice && ./mvnw clean test package
cd ../fleetservice && ./mvnw clean test package
cd ../bookingservice && ./mvnw clean test package

# Step 2: Build Docker Images
docker-compose build

# Step 3: Deploy
docker-compose up -d

# Step 4: Verify
docker-compose ps
curl http://localhost:8081/api/users
curl http://localhost:15672/api/overview -u guest:guest
```

### Pipeline Benefits

- **Automated Testing**: Catches bugs before deployment
- **Consistent Builds**: Same environment every time
- **Fast Feedback**: Know immediately if code breaks
- **Documentation**: Pipeline serves as deployment guide
- **Confidence**: Deploy with certainty

---

## Updated Deployment Instructions

### Prerequisites
* **Docker Desktop** (running)
* **Postman** (for API verification)
* **(Optional) GitHub Account** (for CI/CD)

### Quick Start

```bash
# Clone the repository
git clone <your-repo-url>
cd FlexiDrive-5-microservices-extended

# Start all services including RabbitMQ
docker-compose up --build

# Wait for initialization (30-45 seconds)
# Look for "Started [ServiceName]Application" in logs
```

### Verify Deployment

```bash
# Check all containers are running
docker-compose ps

# Expected output:
# - user-service (8081)
# - fleet-service (8082)
# - booking-service (8083)
# - rabbitmq (5672, 15672)
```

### Access Points

- **User Service**: http://localhost:8081/api/users
- **Fleet Service**: http://localhost:8082/api/vehicles
- **Booking Service**: http://localhost:8083/api/bookings
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)
- **H2 Consoles**: 
  - http://localhost:8081/h2-console
  - http://localhost:8082/h2-console
  - http://localhost:8083/h2-console
