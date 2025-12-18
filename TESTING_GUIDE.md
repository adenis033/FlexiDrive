# Milestone 5 Testing Guide

## Quick Start

1. **Start the system:**
   ```bash
   docker-compose up --build
   ```

2. **Wait for services to initialize** (30-45 seconds)
   Look for these log messages:
   - `Started UserserviceApplication`
   - `Started FleetserviceApplication`
   - `Started BookingserviceApplication`
   - RabbitMQ: `Server startup complete`

## Testing Message Queue Integration

### Test 1: Verify RabbitMQ is Running

1. Open browser: http://localhost:15672
2. Login with `guest` / `guest`
3. Navigate to **Queues** tab
4. You should see: `flexidrive.booking.queue`

### Test 2: Create Booking and Watch Message Flow

#### Step 1: Create a User
```bash
POST http://localhost:8081/api/users
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com",
  "role": "CUSTOMER"
}
```

Response should include `id` (e.g., 1)

#### Step 2: Create a Vehicle
```bash
POST http://localhost:8082/api/vehicles
Content-Type: application/json

{
  "make": "Toyota",
  "model": "Camry",
  "licensePlate": "ABC-123",
  "dailyRate": 50.0,
  "available": true
}
```

Response should include `vehicleId` (e.g., 1)

#### Step 3: Create a Booking (This triggers the message queue!)
```bash
POST http://localhost:8083/api/bookings
Content-Type: application/json

{
  "userId": 1,
  "vehicleId": 1,
  "startDate": "2025-12-20",
  "endDate": "2025-12-25"
}
```

#### Step 4: Observe Message Flow

**In Docker logs:**
```bash
docker-compose logs -f booking-service
```
Look for: `Published BOOKING_CREATED event for booking ID: 1`

```bash
docker-compose logs -f fleet-service
```
Look for: 
- `Received booking event: BOOKING_CREATED for vehicle ID: 1`
- `Vehicle 1 marked as UNAVAILABLE (booked)`

**In RabbitMQ Management UI:**
1. Go to **Queues** tab
2. Click on `flexidrive.booking.queue`
3. Check message rate graph
4. View "Get Message" to inspect payload (if queue still has messages)

#### Step 5: Verify Vehicle Status Changed
```bash
GET http://localhost:8082/api/vehicles/1
```

Response should show: `"available": false`

### Test 3: Cancel Booking (Tests Async Release)

```bash
POST http://localhost:8083/api/bookings/1/cancel
```

**Check logs:**
- BookingService: `Published BOOKING_CANCELLED event for booking ID: 1`
- FleetService: `Vehicle 1 marked as AVAILABLE (booking cancelled)`

**Verify vehicle is available again:**
```bash
GET http://localhost:8082/api/vehicles/1
```

Should show: `"available": true`

## Testing Fault Tolerance (Advanced)

### Scenario: Fleet Service Down

1. Stop Fleet Service:
   ```bash
   docker stop fleet-service
   ```

2. Create a booking (it will fail synchronously, but that's expected)

3. Restart Fleet Service:
   ```bash
   docker start fleet-service
   ```

4. Check if queued messages are processed:
   ```bash
   docker-compose logs fleet-service
   ```

## Testing CI/CD Pipeline

### Option 1: With GitHub

1. Push code to GitHub:
   ```bash
   git add .
   git commit -m "Complete Milestone 5"
   git push origin main
   ```

2. Go to GitHub repository → **Actions** tab
3. Watch the pipeline run (3-5 minutes)
4. Verify all jobs pass: ✅ Build & Test → ✅ Build Docker → ✅ Deploy

### Option 2: Local Simulation

```bash
# Test 1: Build Services
cd bookingservice
./mvnw clean test

# Test 2: Build Docker Images
docker-compose build

# Test 3: Deploy
docker-compose up -d

# Test 4: Health Check
curl http://localhost:8083/api/bookings
curl http://localhost:15672/api/overview -u guest:guest
```

## Monitoring and Debugging

### View All Container Logs
```bash
docker-compose logs -f
```

### View Specific Service Logs
```bash
docker-compose logs -f booking-service
docker-compose logs -f fleet-service
docker-compose logs -f rabbitmq
```

### Check Container Status
```bash
docker-compose ps
```

### Inspect RabbitMQ Queues
```bash
# Get queue statistics
curl -u guest:guest http://localhost:15672/api/queues/%2F/flexidrive.booking.queue

# List all exchanges
curl -u guest:guest http://localhost:15672/api/exchanges
```

### Reset Everything
```bash
docker-compose down -v
docker-compose up --build
```

## Expected Results Summary

✅ **RabbitMQ Integration:**
- Messages published on booking create/cancel
- FleetService receives and processes events
- Vehicle availability updates asynchronously
- RabbitMQ UI shows message flow

✅ **CI/CD Pipeline:**
- All Maven tests pass
- Docker images build successfully
- Services deploy and respond to health checks
- Logs show successful startup

✅ **System Behavior:**
- Create booking → Vehicle becomes unavailable
- Cancel booking → Vehicle becomes available again
- RabbitMQ queues messages when service is down
- Services recover and process pending messages

## Troubleshooting

**Problem:** Services can't connect to RabbitMQ  
**Solution:** Check RabbitMQ health: `docker-compose ps rabbitmq` - wait for "healthy" status

**Problem:** Queue messages not consumed  
**Solution:** Verify FleetService logs for listener errors, check RabbitMQ consumers tab

**Problem:** CI/CD fails on GitHub  
**Solution:** Check Actions tab for detailed error logs, verify all tests pass locally first

**Problem:** Port conflicts  
**Solution:** Stop conflicting services: `netstat -ano | findstr :5672` (Windows) or use different ports
