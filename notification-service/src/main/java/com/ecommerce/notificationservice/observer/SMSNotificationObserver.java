package com.ecommerce.notificationservice.observer;

import com.ecommerce.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SMSNotificationObserver implements EventObserver {

    @Override
    public void update(DomainEvent event) {
        log.info("📱 Sending SMS notification for event: {}", event.getEventType());

        if (event instanceof OrderEvent) {
            OrderEvent orderEvent = (OrderEvent) event;
            sendOrderSMS(orderEvent);
        }
    }

    private void sendOrderSMS(OrderEvent event) {
        // Simulate sending SMS
        String smsText = String.format("Your order #%d is %s. Product: %s",
                event.getOrderId(), event.getStatus(),
                event.getProductId());
        log.info("📱 SMS sent: {}", smsText);
    }

    @Override
    public String getObserverName() {
        return "SMSNotificationObserver";
    }

    @Override
    public boolean isInterestedIn(DomainEvent event) {
        return event instanceof OrderEvent &&
                ("SHIPPED".equals(((OrderEvent) event).getStatus()) ||
                        "COMPLETED".equals(((OrderEvent) event).getStatus()));
    }
}
