public class DailyDrive extends Vehicle {
    private IPricingStrategy pricingStrategy;

    public DailyDrive(String licensePlate, String brand, String model, int year, IPricingStrategy pricingStrategy) {
        super(licensePlate, brand, model, year);
        this.pricingStrategy = pricingStrategy;
    }

    @Override
    public double calculateRentalCost(int duration) {
        return pricingStrategy.calculatePrice(duration);
    }
}
