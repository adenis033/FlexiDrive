public class Supercar extends Vehicle {
    private IPricingStrategy pricingStrategy;

    public Supercar(String licensePlate, String brand, String model, int year, IPricingStrategy pricingStrategy) {
        super(licensePlate, brand, model, year);
        this.pricingStrategy = pricingStrategy;
    }

    @Override
    public double calculateRentalCost(int duration) {
        return pricingStrategy.calculatePrice(duration);
    }
}
