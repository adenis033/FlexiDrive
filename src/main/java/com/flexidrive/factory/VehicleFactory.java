public class VehicleFactory {

    public static Vehicle createVehicle(String type, String licensePlate, String brand, String model, int year, IPricingStrategy pricingStrategy) {
        switch(type.toLowerCase()) {
            case "daily":
                return new DailyDrive(licensePlate, brand, model, year, pricingStrategy);
            case "supercar":
                return new Supercar(licensePlate, brand, model, year, pricingStrategy);
            case "special":
                return new SpecialOccasionVehicle(licensePlate, brand, model, year, pricingStrategy);
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
}
