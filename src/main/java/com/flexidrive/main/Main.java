public class Main {
    public static void main(String[] args) {
        // 1️⃣ Create vehicles using Factory + Strategy pattern
        Vehicle car1 = VehicleFactory.createVehicle("daily", "ABC123", "Toyota", "Corolla", 2020, new PerDayPricing(50));
        Vehicle car2 = VehicleFactory.createVehicle("supercar", "XYZ999", "Ferrari", "488", 2022, new PerHourPricing(100));
        Vehicle car3 = VehicleFactory.createVehicle("special", "SPC001", "Lamborghini", "Huracan", 2023, new PerDayPricing(500));

        // 2️⃣ Setup Observer pattern for car events
        CarEventPublisher publisher = new CarEventPublisher();
        publisher.addObserver(new BillingService());
        publisher.addObserver(new MaintenanceService());

        // 3️⃣ Create bookings using Facade + Decorator patterns
        BookingFacade bookingFacade = new BookingFacade();
        BookingComponent booking1 = bookingFacade.createBooking(car1, true, true); // GPS + Insurance
        BookingComponent booking2 = bookingFacade.createBooking(car2, false, true); // Only Insurance
        BookingComponent booking3 = bookingFacade.createBooking(car3, true, false); // Only GPS

        // 4️⃣ Print booking details
        System.out.println(booking1.getDescription() + " | Total cost for 3 days: " + booking1.getCost(3));
        System.out.println(booking2.getDescription() + " | Total cost for 2 hours: " + booking2.getCost(2));
        System.out.println(booking3.getDescription() + " | Total cost for 1 day: " + booking3.getCost(1));

        // 5️⃣ Notify observers when cars are returned
        publisher.notifyObservers("Car ABC123 returned");
        publisher.notifyObservers("Car XYZ999 returned");
        publisher.notifyObservers("Car SPC001 returned");

        // 6️⃣ Schedule maintenance and cleaning using Facade
        MaintenanceFacade maintenanceFacade = new MaintenanceFacade();
        CleaningFacade cleaningFacade = new CleaningFacade();

        maintenanceFacade.scheduleMaintenance(car1);
        maintenanceFacade.performMaintenance(car1);

        cleaningFacade.scheduleCleaning(car2);
        cleaningFacade.performCleaning(car2);
    }
}
