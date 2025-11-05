public class MaintenanceFacade {

    public void scheduleMaintenance(Vehicle vehicle) {
        System.out.println("Maintenance scheduled for " + vehicle);
    }

    public void performMaintenance(Vehicle vehicle) {
        System.out.println("Performing maintenance on " + vehicle);
    }
}
