public class PerHourPricing implements IPricingStrategy {
    private double hourlyRate;

    public PerHourPricing(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculatePrice(int duration) {
        return hourlyRate * duration;
    }
}
