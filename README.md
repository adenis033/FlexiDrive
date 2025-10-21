# FlexiDrive
**The Dynamic Car Rental System**

---

## Team Members
- Râpa Denis - Andrei
- Bațagoi Ana - Maria

---

## Project Description
FlexiDrive is a professional, implementable Car Rental Management System designed to provide a modern, scalable, and efficient solution for vehicle rental operations. It is built to handle real-world requirements, including multiple user types, dynamic pricing models, vehicle tracking, insurance management, and operational crew coordination.

The system supports multiple **vehicle categories**—Daily Drives, Special Occasion Vehicles, and Supercar Thrill Rides—each with distinct pricing strategies and operational rules. FlexiDrive tracks vehicles in real time, including **current km, condition, maintenance needs, and booking schedules**, ensuring reliability and accuracy for both customers and administrators.

FlexiDrive includes **VIP customer management**, enabling rewards and priority booking for frequent renters. The system also distinguishes **crew roles**, including administrators, booking staff, maintenance personnel, fleet handlers, and cleaning crew, each with permissions aligned to realistic operational responsibilities.

The platform is designed for **extensibility, maintainability, and modularity**, allowing future integration of additional features such as predictive maintenance, advanced billing logic, or GPS-based vehicle tracking, while remaining fully functional with the current core features.

---

## Core Modules & Features

### 1. User Roles
- **Customer:** Book vehicles, view booking history, track km driven, and upgrade to VIP status based on rental frequency.
- **VIP Customer:** Receive priority booking, discounts on rentals and add-ons, and reduced fees.
- **Fleet Crew / Vehicle Handlers:** Handle vehicle check-out and check-in, record km, fuel, and condition.
- **Maintenance Crew:** Inspect vehicles, perform scheduled maintenance, and update maintenance status.
- **Booking Crew:** Assist with booking modifications, optional add-ons, and driver services.
- **Cleaning Crew:** Access vehicle arrival and next booking times to manage cleaning schedules and update availability.
- **Administrator:** Full CRUD for users, vehicles, bookings, pricing rules; assign crew; generate reports; configure system settings.

---

### 2. Vehicle & Fleet Management
- Vehicles have detailed specifications: make, model, year, category, status, current km, maintenance thresholds, and GPS location.
- Fleet inventory allows admins to add, edit, or remove vehicles.
- Maintenance alerts trigger based on km thresholds.
- Vehicle condition and fuel level tracked for each rental.

---

### 3. Booking & Rental Workflow
- **Booking Lifecycle:** Requested $\rightarrow$ Confirmed $\rightarrow$ CheckedOut $\rightarrow$ CheckedIn $\rightarrow$ Closed.
- **Check-Out:** Admin verifies renter credentials and insurance, logs starting km, fuel, and condition.
- **Check-In:** Updates ending km, triggers maintenance alerts, updates availability, finalizes billing.
- **Add-Ons:** GPS, child seat, premium insurance, or driver service.
- **VIP Benefits:** Applied automatically based on loyalty program rules.

---

### 4. Pricing & Add-Ons
- **Pricing Strategies (Strategy Pattern):**
  - PerDayPricing for Daily Drives
  - PerHourPricing for Supercars
  - PackagePricing for Special Occasion Vehicles
- **Add-Ons (Decorator Pattern):**
  - GPS, Premium Insurance, Child Seat, Roadside Assistance
  - Dynamically adjust total cost and description without creating multiple subclasses.

---

### 5. Insurance & Billing
- Base insurance mandatory for all bookings.
- Optional upgrades for extended coverage.
- Billing accounts for base rental, add-ons, extra km, late fees, and damages.

---

### 6. Event-Driven Updates (Observer Pattern)
- **CarReturnedEvent** triggers updates in:
  - Billing (final charges, extra km fees)
  - Maintenance (service scheduling)
  - Crew notifications (cleaning and availability updates)
- **Extensibility:** New observers can be added without modifying existing core logic.

---

### 7. Crew & Admin Analytics
- Vehicle utilization by category.
- Maintenance schedule overview and alerts.
- Revenue and booking statistics.
- VIP vs standard customer activity reports.

---

### 8. Design Patterns & Justifications

1.  **Factory Method Pattern**
    - **Problem:** Vehicles differ by type and attributes; creation must be decoupled from client modules.
    - **Solution:** `VehicleFactory` defines creation methods; concrete factories handle `DailyDrive`, `Supercar`, and `SpecialOccasion` instantiation.
    - **Justification:** Supports extensibility and maintains Open/Closed Principle. Adding new vehicle types requires no changes to existing code.
    - **Advantage:** Avoids fragile if-else statements; simplifies maintenance and testing.

2.  **Strategy Pattern**
    - **Problem:** Pricing varies by vehicle category and rental duration.
    - **Solution:** `IPricingStrategy` interface with multiple implementations (PerDay, PerHour, Package).
    - **Justification:** Isolates pricing logic; Booking class delegates calculation to assigned strategy.
    - **Advantage:** Eliminates complex conditionals; enables dynamic pricing adjustments like weekend specials or VIP discounts.

3.  **Decorator Pattern**
    - **Problem:** Optional add-ons and insurance must be dynamically applied.
    - **Solution:** Decorators wrap core `Booking` object (GPS, PremiumInsurance, ChildSeat).
    - **Justification:** Avoids class explosion; allows runtime configuration.
    - **Advantage:** Simplifies maintenance, modular and reusable.

4.  **Observer Pattern**
    - **Problem:** Multiple subsystems must respond to vehicle check-in without tight coupling.
    - **Solution:** `CarReturnedEvent` notifies all subscribed observers.
    - **Justification:** Loose coupling ensures maintainability; new observers added without modifying core logic.
    - **Advantage:** Prevents fragile monolithic methods; supports scalable, event-driven updates.

5.  **Facade Pattern**
    - **Problem:** The system is complex, with many subsystems (Booking, Fleet, Maintenance, Billing). Different crew roles (e.g., Cleaning Crew, Maintenance Crew) only need to perform a small, specific set of tasks. Exposing the entire complex system to them is insecure and overwhelming.
    - **Solution:** Create a specific `Facade` for each role. For example, a `CleaningCrewFacade` would only expose methods like `getCleaningSchedule()` and `markVehicleAsClean()`. This facade internally communicates with the more complex Fleet and Booking subsystems but hides that complexity from the crew member.
    - **Justification:** This directly addresses the requirement for "crew with different tasks that will have access to do different stuff." It simplifies the interface for each user role, enforcing the **Principle of Least Privilege**.
    - **Advantage:** Improves security by restricting access. Drastically simplifies the workflow for non-admin users. Decouples the user-facing part of the application from the complex internal subsystems, making the system easier to maintain.

---

### 9. Expected Benefits
- **Realism:** Roles, km/fuel tracking, cleaning schedules, and maintenance reflect real-world operations.
- **Extensibility:** Easily add new vehicle types, pricing strategies, or crew roles.
- **Maintainability:** Modular design patterns reduce duplication and enable independent testing.
- **Reusability:** Strategies, decorators, and observers are reusable across modules.
- **Scalability:** Can evolve into an enterprise-grade multi-location system.
- **Reliability:** Event-driven updates ensure accurate fleet, billing, and crew coordination.

---

### 10. Conclusion
FlexiDrive is a fully implementable, professional car rental system that realistically models customer workflows, VIP programs, multiple crew roles, vehicle tracking, maintenance, and billing. By leveraging **Factory, Strategy, Decorator, Observer, and Facade patterns**, the system achieves modularity, maintainability, and extensibility.

The design balances realism and software engineering best practices, making it suitable for professional-level implementation while supporting future feature expansions.
