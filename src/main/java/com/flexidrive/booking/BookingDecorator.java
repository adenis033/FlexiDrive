public abstract class BookingDecorator extends BookingComponent {
    protected BookingComponent decoratedBooking;

    public BookingDecorator(BookingComponent booking) {
        super(booking.vehicle);
        this.decoratedBooking = booking;
    }
}
