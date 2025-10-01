package com.ecommerce.notificationservice.listener;

import com.ecommerce.Config.RabbitMQConfig;
import com.ecommerce.event.*;
import com.ecommerce.notificationservice.publisher.EventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventMessageListener {
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues= RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderEvent(String message) {
        try {
            log.info("📨 Received order event message {}", message);

            // Parse the message and create OrderEvent
            OrderEvent event = objectMapper.readValue(message, OrderEvent.class);

            // Publish to observers
            eventPublisher.publish(event);

        } catch (Exception e) {
            log.error("Error processing order event: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_QUEUE)
    public void handleProductEvent(String message) {
        try {
            log.info("Received product event message");
            var productEvent = objectMapper.readValue(message, ProductEvent.class);
            eventPublisher.publish(productEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_QUEUE)
    public void handleInventoryEvent(String message) {
        try {
            log.info("Received Inventory event message");
            var inventoryEvent = objectMapper.readValue(message, InventoryEvent.class);
            eventPublisher.publish(inventoryEvent);
        }
        catch (Exception e) {
            log.error("Error processing inventory event: {}", e.getMessage(), e);
        }
    }
}
