package Server;

import java.util.EventListener;

public interface Publishable {
    void subscribe(String eventType, EventListener listener);
    void unsubscribe(String eventType, EventListener listener);
    void notify(String eventType);
}
