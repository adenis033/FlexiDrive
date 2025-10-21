# FlexiDrive  
### The Dynamic Car Rental System  

---

## Team Members
- **Râpa Denis-Andrei**  
- **Bațagoi Ana-Maria**  

---

## Project Description

**FlexiDrive** is an intelligent, scalable, and modular **Car Rental Management System** designed to transform traditional rental operations into a modern, flexible, and automated platform.  

The system supports a **multi-tier vehicle classification model**, allowing customers to select from three primary categories:
- **Daily Drives:** Standard cars (sedans, hatchbacks, SUVs) for day-to-day or business use.  
- **Special Occasion Vehicles:** Premium or event-oriented cars (limousines, luxury sedans, vintage models) designed for special occasions, often rented with a driver.  
- **Supercar Thrill Rides:** High-performance sports cars available for short time blocks (30 min, 1 hour, or 2 hours).  

FlexiDrive manages the **entire rental lifecycle** — from user registration, browsing, and booking to insurance verification, billing, and vehicle return. The architecture prioritizes **extensibility, maintainability, and decoupling**, allowing for future integrations such as:
- AI-based demand forecasting,
- Dynamic pricing adjustments,
- IoT-enabled vehicle tracking.  

This approach ensures a **robust, future-proof system** that can evolve toward an enterprise-grade multi-location platform.

---

## Core Modules & Features

### 1. Vehicle & Fleet Management
The fleet management module handles all operations related to vehicle data, status, and pricing.

- **Dynamic Vehicle Categories:**  
  Each vehicle type (Daily Drive, Special Occasion, Supercar) has its own constraints and pricing rules.  
- **Fleet Inventory Management:**  
  Administrators can add, modify, or remove vehicles, tracking details such as model, year, mileage, and availability.  
- **Dynamic Pricing Engine:**  
  Supports multiple pricing models — per-day, per-hour, or event-based packages. Prices automatically adapt to insurance type, add-ons, and promotions.

### 2. Booking & Rental Workflow
The booking process is state-driven and ensures consistency across all rentals.

- **User Authentication & Profiles:**  
  Users must register, verify their identity, and store valid license and payment data.  
- **Vehicle Search & Reservation:**  
  Real-time filtering by category, location, and date. Integration with the pricing engine ensures accurate cost calculations.  
- **Rental Check-Out:**  
  Admin validates user credentials and logs vehicle condition, mileage, and fuel level before release.  
- **Rental Check-In:**  
  Upon return, the system updates vehicle status, calculates additional fees if needed, and triggers maintenance or cleaning workflows.

### 3. Insurance & Add-ons
The insurance module ensures legal compliance and customer customization.

- **Mandatory Insurance Integration:**  
  Every booking includes a base insurance policy (e.g., liability or comprehensive).  
- **Optional Add-ons:**  
  Customers can enhance their booking with GPS, child seats, premium insurance, or roadside assistance.  
  Add-ons dynamically modify total pricing and descriptions through the **Decorator Pattern**.

---

## Selected Design Patterns & Justifications

### 1. Factory Method Pattern
**Problem:**  
The system must create different vehicle types (`DailyDrive`, `Supercar`, `SpecialOccasion`) that share a common interface but differ in configuration and attributes.  

**Solution:**  
A `VehicleFactory` defines a creation interface, while concrete factories (`DailyDriveFactory`, `SupercarFactory`, etc.) handle object instantiation logic.  

**Justification:**  
The Factory Method decouples object creation from business logic in modules like Booking or Fleet Management, allowing new vehicle types (e.g., “ElectricBike”) to be added without changing existing code.

**Comparison to simpler alternatives:**  
Using conditional logic (`if-else` or `switch`) violates the *Open/Closed Principle* — every time a new category is introduced, the creation logic must be modified. The Factory Method eliminates this dependency, improving scalability and maintainability.

---

### 2. Strategy Pattern
**Problem:**  
Each vehicle category uses a different pricing model — per-day, per-hour, or flat-package rates.

**Solution:**  
Define a common `IPricingStrategy` interface with `calculatePrice(BookingDetails details)` and implement multiple concrete strategies:  
- `PerDayPricingStrategy`  
- `PerHourPricingStrategy`  
- `PackagePricingStrategy`  

**Justification:**  
The Strategy Pattern encapsulates each pricing algorithm, keeping the `Booking` class simple and flexible. Different strategies can be assigned dynamically based on vehicle type.

**Comparison to simpler alternatives:**  
Without Strategy, pricing logic would be implemented as nested conditionals inside the `Booking` class, making the system rigid and error-prone. Strategy allows easy integration of future models such as “WeekendDiscountStrategy” without altering existing code.

---

### 3. Decorator Pattern
**Problem:**  
Bookings require mandatory insurance and may include optional add-ons that affect both description and price.  

**Solution:**  
Use decorators such as `BasicInsuranceDecorator`, `GpsDecorator`, and `PremiumInsuranceDecorator`, each wrapping a core `Booking` object and adding cost or functionality dynamically.  

**Justification:**  
The Decorator Pattern enables flexible feature combinations at runtime without subclass explosion (e.g., `BookingWithGpsAndInsurance`).  

**Comparison to simpler alternatives:**  
Implementing each combination as a subclass leads to code duplication and complexity. Using boolean flags (`hasGps`, `hasInsurance`) also clutters logic. Decorators maintain code clarity and flexibility while following the *Open/Closed Principle*.

---

### 4. Observer Pattern
**Problem:**  
When a car is returned, multiple subsystems must react — mileage update, billing, maintenance, and notifications.  

**Solution:**  
Implement a `CarReturnedEvent` subject that notifies registered observers:  
- `VehicleStatusObserver`  
- `BillingObserver`  
- `MaintenanceObserver`  
- `MarketingEmailObserver`  

**Justification:**  
The Observer Pattern supports loose coupling between components. The FleetOperations module doesn’t need to know which subsystems depend on the event.  

**Comparison to simpler alternatives:**  
Without Observer, the `checkInVehicle()` method would explicitly call each subsystem (`billing.finalize()`, `maintenance.checkService()`), leading to tight coupling and poor scalability. Observer allows seamless integration of new modules like analytics or customer feedback without modifying existing code.

---

## Additional Architectural Patterns

### Singleton Pattern
Used for shared services like:
- `DatabaseConnectionManager`
- `FleetInventoryService`

Ensures a single instance across the system, centralizing data access and improving performance consistency.

### Model-View-Controller (MVC)
The system follows MVC for clear separation of concerns:
- **Model:** Core business entities (Vehicle, Booking, Insurance, User).  
- **View:** User-facing web or mobile interfaces.  
- **Controller:** Connects user actions with application logic and data persistence.

This architecture simplifies maintenance, testing, and potential expansion into multiple platforms.

---

## Expected Benefits
- **Extensibility:** New vehicle types, features, or pricing strategies can be added easily.  
- **Maintainability:** Decoupled modules reduce interdependencies.  
- **Reusability:** Strategies, decorators, and observers are reusable across multiple modules.  
- **Scalability:** Architecture supports enterprise-level fleet management.  
- **Reliability:** Event-driven updates ensure consistent billing and vehicle state tracking.

---

## Conclusion
**FlexiDrive** demonstrates how structured design and pattern-oriented development produce scalable, flexible, and maintainable software.  

By combining **Factory**, **Strategy**, **Decorator**, and **Observer** patterns, the system adheres to **SOLID principles**, ensuring long-term adaptability and professional-grade design quality.  

Future enhancements may include:
- Machine learning–based demand forecasting,  
- Automated fleet optimization,  
- IoT integration for real-time vehicle tracking.  

This demonstrates how thoughtful architectural planning allows the system to evolve alongside modern technological trends.

---
