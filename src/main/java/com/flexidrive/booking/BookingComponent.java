public abstract class BookingComponent {
    protected Vehicle vehicle;

    public BookingComponent(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public abstract double getCost(int duration);
    public abstract String getDescription();
}
