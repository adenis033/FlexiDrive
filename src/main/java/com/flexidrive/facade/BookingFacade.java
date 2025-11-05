public class BookingFacade {
    public BookingComponent createBooking(Vehicle vehicle, boolean gps, boolean insurance) {
        BookingComponent booking = new Booking(vehicle);
        if(gps) booking = new GPSDecorator(booking);
        if(insurance) booking = new PremiumInsuranceDecorator(booking);
        return booking;
    }
}
