# FlexiDrive - **The Dynamic Car Rental System** | Milestone 2

---

## Team Members
- Râpa Denis - Andrei
- Bațagoi Ana - Maria
- Mazilu Stefan

---

## Project Description
FlexiDrive is a **dynamic car rental system** designed to manage vehicle bookings, optional features, maintenance, and cleaning.

This milestone demonstrates the **core structure and interactions** of the system, focusing on **four key design patterns** and a **layered architecture**. It is a **proof-of-concept implementation** for Milestone 2, showcasing the system’s main functionalities.

---

## Project Structure and Layers

### 1. Vehicle Layer (Factory + Strategy)
- **Vehicle** (abstract) — base class for all vehicles.  
- **Concrete Vehicles**:  
  - `DailyDrive` — standard rental car.  
  - `Supercar` — high-performance car.  
  - `SpecialOccasionVehicle` — luxury/special occasion car.  
- **Pricing Strategy (`IPricingStrategy`)**: defines how rental cost is calculated. Implementations:  
  - `PerDayPricing`  
  - `PerHourPricing`  
  - `PackagePricing`  
- **VehicleFactory**: creates vehicles of different types with default pricing.

**Example Base Rates (per day):**

| Vehicle Type | Rate |
|-------------|------|
| DailyDrive | 50 |
| Supercar | 150 |
| SpecialOccasionVehicle | 250 |

---

### 2. Booking Layer (Decorator + Facade)
- **BookingComponent** — base abstraction for bookings.  
- **Booking** — concrete booking implementation for a vehicle.  
- **Decorators**:  
  - `GPSDecorator` — adds GPS to a booking.  
  - `PremiumInsuranceDecorator` — adds insurance coverage.  
- **BookingFacade** — simplifies the creation of bookings with optional features.

**Example Usage:**
```bash
BookingComponent booking = bookingFacade.createBooking(car, true, true)
total = booking.getCost(3) # total cost for 3 days with GPS and insurance
```

---

### 3. Maintenance & Cleaning Layer (Facade)
- **MaintenanceFacade** — schedules and executes maintenance for vehicles.  
- **CleaningFacade** — schedules and executes cleaning for vehicles.  
- These facades provide a simplified interface for operational tasks, hiding the complexity of multiple services.

**Example Usage:**
```bash
maintenanceFacade.scheduleMaintenance(car)
maintenanceFacade.performMaintenance(car)

cleaningFacade.scheduleCleaning(car)
cleaningFacade.performCleaning(car)
```

---

### 4. Event System Layer (Observer)
- **CarEventPublisher** — publishes events like “car returned”.  
- **Observers**:  
  - `BillingService` — handles billing operations when cars are returned.  
  - `MaintenanceService` — schedules maintenance when cars are returned.  

**Flow Example:**
```bash
publisher = CarEventPublisher()
publisher.addObserver(BillingService())
publisher.addObserver(MaintenanceService())

publisher.notifyObservers("Car XYZ999 returned")
```

---

## Design Patterns Demonstrated
1. **Factory** — `VehicleFactory` creates different vehicle types.  
2. **Strategy** — `IPricingStrategy` allows flexible pricing calculation.  
3. **Decorator** — optional features like GPS and insurance can be added dynamically.  
4. **Facade** — simplifies interactions with complex systems (Booking, Maintenance, Cleaning).  
5. **Observer** — services react automatically to vehicle events (car returned).  

---

## Milestone 2 Scope
- Implemented **core vehicle classes** and pricing strategies.  
- Implemented **booking system** with decorators for optional features.  
- Implemented **facades** for booking, maintenance, and cleaning.  
- Implemented **observer pattern** for handling car events.  
- Provides a **proof-of-concept demonstration** of cost calculation, booking creation, and service notifications.
