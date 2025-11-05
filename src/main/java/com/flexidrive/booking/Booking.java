public class Booking extends BookingComponent {

    public Booking(Vehicle vehicle) {  // No import needed
        super(vehicle);
    }

    @Override
    public double getCost(int duration) {
        return vehicle.calculateRentalCost(duration);
    }

    @Override
    public String getDescription() {
        return "Booking for: " + vehicle;
    }
}
