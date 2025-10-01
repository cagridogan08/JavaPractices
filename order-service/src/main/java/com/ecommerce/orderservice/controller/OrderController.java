package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.Order;
import com.ecommerce.orderservice.dto.UpdateOrderRequest;
import com.ecommerce.orderservice.service.ElasticsearchService;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {
    private final OrderService orderService;
    private final ElasticsearchService elasticsearchService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Order>> searchOrders(@RequestParam String productId) {
        return ResponseEntity.ok(elasticsearchService.searchOrders(productId));
    }
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @DeleteMapping
    public ResponseEntity deleteOrder(@RequestParam Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("");
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long orderId,
            @RequestParam String status) {
        var order = new UpdateOrderRequest();
        order.setOrderId(orderId);
        order.setStatus(status);
        return ResponseEntity.ok(orderService.updateOrderStatus(order));

    }
}
