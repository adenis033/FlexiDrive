public class PremiumInsuranceDecorator extends BookingDecorator {
    public PremiumInsuranceDecorator(BookingComponent booking) {
        super(booking);
    }

    @Override
    public double getCost(int duration) {
        return decoratedBooking.getCost(duration) + 20 * duration;
    }

    @Override
    public String getDescription() {
        return decoratedBooking.getDescription() + " + Premium Insurance";
    }
}
