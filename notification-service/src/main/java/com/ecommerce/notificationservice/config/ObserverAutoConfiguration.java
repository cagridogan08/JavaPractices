package com.ecommerce.notificationservice.config;

import com.ecommerce.notificationservice.observer.EventObserver;
import com.ecommerce.notificationservice.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ObserverAutoConfiguration {

    private final EventPublisher eventPublisher;
    private final List<EventObserver> observers;

    @EventListener(ApplicationReadyEvent.class)
    public void registerObservers() {
        log.info("🚀 Auto-registering {} observers", observers.size());

        for (EventObserver observer : observers) {
            eventPublisher.subscribe(observer);
        }

        log.info("✅ All observers registered successfully");
    }
}