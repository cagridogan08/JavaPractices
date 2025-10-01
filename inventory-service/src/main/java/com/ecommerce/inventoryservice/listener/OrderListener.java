package com.ecommerce.inventoryservice.listener;

import com.ecommerce.Config.RabbitMQConfig;
import com.ecommerce.event.OrderEvent;
import com.ecommerce.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderListener {
    private final InventoryService  inventoryService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderMessage(String orderMessage) {
        try {
            var order = objectMapper.readValue(orderMessage, OrderEvent.class);
            log.info("Received Order Message {}", order);
            var reserved = inventoryService.reserveInventory(order.getProductId(), order.getQuantity());


            if(reserved){
                log.info("Reserved Order Message {}", order);
            }
            else{
                log.info("Not Reserved Order Message {}", order);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
