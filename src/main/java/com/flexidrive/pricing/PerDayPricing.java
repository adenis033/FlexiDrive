public class PerDayPricing implements IPricingStrategy {
    private double dailyRate;

    public PerDayPricing(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    @Override
    public double calculatePrice(int duration) {
        return dailyRate * duration;
    }
}
