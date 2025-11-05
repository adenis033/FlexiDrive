public class GPSDecorator extends BookingDecorator {
    public GPSDecorator(BookingComponent booking) {
        super(booking);
    }

    @Override
    public double getCost(int duration) {
        return decoratedBooking.getCost(duration) + 5 * duration;
    }

    @Override
    public String getDescription() {
        return decoratedBooking.getDescription() + " + GPS";
    }
}
