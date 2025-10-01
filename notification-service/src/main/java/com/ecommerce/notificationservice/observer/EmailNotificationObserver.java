package com.ecommerce.notificationservice.observer;

import com.ecommerce.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificationObserver implements EventObserver {

    @Override
    public void update(DomainEvent event) {
        log.info("📧 Sending email notification for event: {}", event.getEventType());

        if (event instanceof OrderEvent) {
            OrderEvent orderEvent = (OrderEvent) event;
            sendOrderEmail(orderEvent);
        }
    }

    private void sendOrderEmail(OrderEvent event) {
        // Simulate sending email
        log.info("📧 Email sent to customer: Order {} is now {}",
                event.getOrderId(), event.getStatus());

        String emailContent = String.format("""
            Dear Customer,
            
            Your order #%d has been %s.
            Product: %s
            Quantity: %d
            
            Thank you for your order!
            """, event.getOrderId(), event.getStatus(),
                event.getProductId(), event.getQuantity());

        log.debug("Email content: {}", emailContent);
    }

    @Override
    public String getObserverName() {
        return "EmailNotificationObserver";
    }

    @Override
    public boolean isInterestedIn(DomainEvent event) {
        return event instanceof OrderEvent;
    }
}
