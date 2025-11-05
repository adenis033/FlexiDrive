import java.util.ArrayList;
import java.util.List;

public class CarEventPublisher {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) { observers.add(o); }
    public void removeObserver(Observer o) { observers.remove(o); }

    public void notifyObservers(String event) {
        for (Observer o : observers) {
            o.update(event);
        }
    }
}
