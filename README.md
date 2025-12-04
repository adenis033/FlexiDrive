# FlexiDrive | Distributed Vehicle Rental Platform

A scalable, microservices-based backend solution developed for Milestone 4. This project demonstrates distributed system design, inter-service orchestration, and containerized deployment.

---

## Team Members
- Râpa Denis - Andrei
- Bațagoi Ana - Maria
- Mazilu Stefan

---

## System Architecture

The solution is decomposed into three autonomous microservices, each strictly adhering to the Single Responsibility Principle. Each service manages its own data persistence and communicates over a RESTful network.

### 1. User Identity Service
* **Port:** `8081`
* **Role:** The authority for customer data.
* **Function:** Handles user registration and retrieval. In a real-world scenario, this would also handle JWT generation and role-based access control (RBAC).

### 2. Fleet Inventory Service
* **Port:** `8082`
* **Role:** The system of record for vehicle assets.
* **Function:** Manages the lifecycle of vehicles and, crucially, maintains the **real-time availability state** (Boolean logic) of every car in the fleet.

### 3. Booking Orchestration Service
* **Port:** `8083`
* **Role:** The transaction processor.
* **Function:** Validates business logic by coordinating with the other two services. It ensures a user is valid and a vehicle is free before committing a reservation. It also handles the **state transition logic** (locking/unlocking cars).

---

## Inter-Service Logic & Data Flow

The core complexity of this milestone lies in how the services synchronize state without a shared database.

* **Reservation Workflow:**
    1.  Incoming `POST` request to **Booking Service**.
    2.  **Booking** → `GET` **User Service** (Validates User ID).
    3.  **Booking** → `GET` **Fleet Service** (Validates Vehicle ID).
    4.  **Booking** → `PUT` **Fleet Service** (Updates vehicle status to `available: false`).
* **Cancellation Workflow:**
    1.  Incoming `POST` cancel request to **Booking Service**.
    2.  **Booking** updates internal status to `CANCELLED`.
    3.  **Booking** → `PUT` **Fleet Service** (Releases vehicle, setting `available: true`).

---

## Deployment Instructions

### Prerequisites
* **Docker Desktop** (running)
* **Postman** (for API verification)

### Phase 1: System Startup
We use Docker Compose to orchestrate the build and network bridging.

1.  Clone the repository and navigate to the root directory.
2.  Execute the build & run command:
    ```bash
    docker-compose up --build
    ```
3.  **Wait for Initialization:** The system is ready when you see "Started [ApplicationName]" logs for all three containers.

### Phase 2: Verification
Check that the container cluster is active:
```bash
docker ps
````

You should see:

  * `flexidrive-booking`
  * `flexidrive-fleet`
  * `flexidrive-user`

-----

## Quality Assurance (Testing)

A complete test suite is provided in the `flexidrive_postman.json` file.

### Import Procedure

1.  Open Postman.
2.  Drag and drop `flexidrive_postman.json` into the window.

### Execution Sequence

For the logic to hold, tests must be run in this specific order to simulate a real user journey:

1.  **User Provisioning:** Run `Create User` (User Service).
2.  **Fleet Stocking:** Run `Add Vehicle` (Fleet Service).
      * *Checkpoint:* Vehicle should be Available.
3.  **Transaction Execution:** Run `Create Booking` (Booking Service).
      * *Checkpoint:* Check Vehicle Status. It must now be **Unavailable**.
4.  **Compensation Logic:** Run `Cancel Booking` (Booking Service).
      * *Checkpoint:* Check Vehicle Status. It must return to **Available**.

-----

## Operational Management

**To stop the specific cluster:**

```bash
docker-compose down
```

**To perform a deep clean (removes containers, networks, and volumes):**

```bash
docker-compose down -v
```

## Troubleshooting Guide

  * **Port Conflicts:** Ensure ports 8081-8083 are free. If `Tomcat` fails to bind, check for zombie java processes or change ports in `application.properties` AND `docker-compose.yml`.
  * **Network Timeouts:** If the Booking Service fails to call the User Service immediately on startup, wait 10 seconds. The containerized JVMs may initialize at different speeds.

## Technology Stack

  * **Runtime:** Java 17 (Amazon Corretto)
  * **Framework:** Spring Boot 3.3
  * **Containerization:** Docker & Docker Compose
  * **Database:** H2 (In-Memory for development speed)
  * **Transport:** REST / JSON
