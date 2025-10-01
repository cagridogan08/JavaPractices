package com.ecommerce.notificationservice.observer;

import com.ecommerce.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WebhookNotificationObserver implements EventObserver {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void update(DomainEvent event) {
        log.info("🔗 Triggering webhook for event: {}", event.getEventType());
        sendWebhook(event);
    }

    private void sendWebhook(DomainEvent event) {
        try {
            // Simulate webhook call
            String webhookUrl = "https://webhook.site/your-webhook-url";
            log.info("🔗 Webhook called: {} with event: {}", webhookUrl, event.getEventType());

            // Uncomment to actually send webhook
            // restTemplate.postForEntity(webhookUrl, event, String.class);

        } catch (Exception e) {
            log.error("Failed to send webhook: {}", e.getMessage());
        }
    }

    @Override
    public String getObserverName() {
        return "WebhookNotificationObserver";
    }

    @Override
    public boolean isInterestedIn(DomainEvent event) {
        // Webhooks for all events
        return true;
    }
}

