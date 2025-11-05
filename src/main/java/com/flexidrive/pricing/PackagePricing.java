public class PackagePricing implements IPricingStrategy {
    private double packageRate;

    public PackagePricing(double packageRate) {
        this.packageRate = packageRate;
    }

    @Override
    public double calculatePrice(int duration) {
        return packageRate;
    }
}
