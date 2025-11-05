public class CleaningFacade {

    public void scheduleCleaning(Vehicle vehicle) {
        System.out.println("Cleaning scheduled for " + vehicle);
    }

    public void performCleaning(Vehicle vehicle) {
        System.out.println("Performing cleaning on " + vehicle);
    }
}
