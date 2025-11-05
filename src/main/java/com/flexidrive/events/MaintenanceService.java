public class MaintenanceService implements Observer {
    @Override
    public void update(String event) {
        System.out.println("MaintenanceService received event: " + event);
    }
}
