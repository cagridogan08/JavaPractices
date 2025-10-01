package com.ecommerce.notificationservice.observer;

import com.ecommerce.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SlackNotificationObserver implements EventObserver {

    @Override
    public void update(DomainEvent event) {
        log.info("💬 Sending Slack notification for event: {}", event.getEventType());

        if (event instanceof InventoryEvent) {
            InventoryEvent invEvent = (InventoryEvent) event;
            if ("LOW_STOCK".equals(invEvent.getAction())) {
                sendLowStockAlert(invEvent);
            }
        }
    }

    private void sendLowStockAlert(InventoryEvent event) {
        String slackMessage = String.format(
                "⚠️ LOW STOCK ALERT!\n" +
                        "Product: %s\n" +
                        "Available: %d units\n" +
                        "Reserved: %d units",
                event.getProductId(),
                event.getQuantity() - event.getReservedQuantity(),
                event.getReservedQuantity()
        );

        log.warn("💬 Slack alert: {}", slackMessage);
    }

    @Override
    public String getObserverName() {
        return "SlackNotificationObserver";
    }

    @Override
    public boolean isInterestedIn(DomainEvent event) {
        return event instanceof InventoryEvent;
    }
}
