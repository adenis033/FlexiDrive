public class BillingService implements Observer {
    @Override
    public void update(String event) {
        System.out.println("BillingService received event: " + event);
    }
}
