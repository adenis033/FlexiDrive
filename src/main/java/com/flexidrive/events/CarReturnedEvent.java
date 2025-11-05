public class CarReturnedEvent {
    private CarEventPublisher publisher;

    public CarReturnedEvent(CarEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void triggerEvent(String vehicleInfo) {
        // Notify all observers that a car has been returned
        publisher.notifyObservers("Car returned: " + vehicleInfo);
    }
}
