package com.ecommerce.orderservice.service;

import com.ecommerce.Config.RabbitMQConfig;
import com.ecommerce.Order;
import com.ecommerce.event.OrderEvent;
import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.UpdateOrderRequest;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final ElasticsearchService elasticsearchService;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order createOrder(CreateOrderRequest order) {
        var requestedOrder = new Order();
        requestedOrder.setQuantity(order.getQuantity());
        requestedOrder.setProductId(order.getProductId());

        Order savedOrder = orderRepository.saveAndFlush(requestedOrder);

        // Don't catch exceptions here - let them propagate for rollback
        publishOrderEventAsync(savedOrder, "ORDER_CREATED");
        indexOrderAsync(savedOrder);

        return savedOrder;
    }

    @Transactional
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Transactional
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order updateOrderStatus(UpdateOrderRequest request) {
        // Fetch the order within the transaction

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + request.getOrderId()));

        // Update status
        order.setStatus(request.getStatus());

        // Save and flush
        Order updatedOrder = orderRepository.saveAndFlush(order);

        // Publish event after successfully save
        publishOrderEventAsync(updatedOrder, "ORDER_STATUS_UPDATED");

        // Update in Elasticsearch
        indexOrderAsync(updatedOrder);

        return updatedOrder;
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Delete from database
        orderRepository.delete(order);
        orderRepository.flush();

        // Delete it from Elasticsearch (async)
        deleteOrderFromElasticsearchAsync(id.toString());

        // Publish deletion event
        publishOrderEventAsync(order, "ORDER_DELETED");
    }

    // Async method to avoid transaction issues with external systems
    private void publishOrderEventAsync(Order order, String eventType) {
        try {
            OrderEvent event = new OrderEvent(order, eventType);
            String message = objectMapper.writeValueAsString(event);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_ROUTING_KEY,
                    message
            );

            log.info("Published event: {} for order: {}", eventType, order.getId());
        } catch (Exception e) {
            log.error("Error publishing order event: {}", e.getMessage(), e);
            // Don't throw exception - don't want to rollback transaction
        }
    }

    private void indexOrderAsync(Order order) {
        try {
            elasticsearchService.indexOrder(order);
        } catch (Exception e) {
            log.error("Error indexing order in Elasticsearch: {}", e.getMessage(), e);
            // Don't throw - continue even if Elasticsearch fails
        }
    }

    private void deleteOrderFromElasticsearchAsync(String orderId) {
        try {
            elasticsearchService.deleteOrder(orderId);
        } catch (Exception e) {
            log.error("Error deleting order from Elasticsearch: {}", e.getMessage(), e);
        }
    }

    public List<Order> searchOrders(String productId, String status) {
        if (status != null && !status.isEmpty()) {
            return elasticsearchService.searchOrdersByProductIdAndStatus(productId, status);
        }
        return elasticsearchService.searchOrders(productId);
    }


}
