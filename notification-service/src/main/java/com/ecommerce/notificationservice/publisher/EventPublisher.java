package com.ecommerce.notificationservice.publisher;

import com.ecommerce.event.DomainEvent;
import com.ecommerce.notificationservice.observer.EventObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class EventPublisher {

    private final List<EventObserver> observers = new CopyOnWriteArrayList<>();

    public void subscribe(EventObserver observer) {
        observers.add(observer);
        log.info("✅ Observer subscribed: {}", observer.getObserverName());
    }

    public void unsubscribe(EventObserver observer) {
        observers.remove(observer);
        log.info("❌ Observer unsubscribed: {}", observer.getObserverName());
    }

    @Async
    public void publish(DomainEvent event) {
        log.info("📢 Publishing event: {} from {}",
                event.getEventType(), event.getSource());

        int notifiedCount = 0;
        for (EventObserver observer : observers) {
            if (observer.isInterestedIn(event)) {
                try {
                    observer.update(event);
                    notifiedCount++;
                } catch (Exception e) {
                    log.error("Error notifying observer {}: {}",
                            observer.getObserverName(), e.getMessage());
                }
            }
        }

        log.info("✅ Event published to {} observers", notifiedCount);
    }

    public List<String> getSubscribers() {
        List<String> subscriberNames = new ArrayList<>();
        for (EventObserver observer : observers) {
            subscriberNames.add(observer.getObserverName());
        }
        return subscriberNames;
    }
}
