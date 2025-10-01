package com.ecommerce.notificationservice.observer;

import com.ecommerce.event.DomainEvent;

public interface EventObserver {
    void update(DomainEvent event);
    String getObserverName();
    boolean isInterestedIn(DomainEvent event);
}
