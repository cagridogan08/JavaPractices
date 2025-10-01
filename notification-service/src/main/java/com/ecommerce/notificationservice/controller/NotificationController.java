package com.ecommerce.notificationservice.controller;

import com.ecommerce.event.InventoryEvent;
import com.ecommerce.event.OrderEvent;
import com.ecommerce.event.ProductEvent;
import com.ecommerce.notificationservice.publisher.EventPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Notifications", description = "Notification subscription management")
public class NotificationController {

    private final EventPublisher eventPublisher;

    @Operation(summary = "Get all subscribers")
    @GetMapping("/subscribers")
    public ResponseEntity<List<String>> getSubscribers() {
        return ResponseEntity.ok(eventPublisher.getSubscribers());
    }

    @Operation(summary = "Test order notification")
    @PostMapping("/test/order")
    public ResponseEntity<String> testOrderNotification(@RequestBody OrderEvent event) {
        eventPublisher.publish(event);
        return ResponseEntity.ok("Order notification sent to all subscribers");
    }

    @Operation(summary = "Test inventory notification")
    @PostMapping("/test/inventory")
    public ResponseEntity<String> testInventoryNotification(@RequestBody InventoryEvent event) {
        eventPublisher.publish(event);
        return ResponseEntity.ok("Inventory notification sent to all subscribers");
    }

    @Operation(summary = "Test product notification")
    @PostMapping("/test/product")
    public ResponseEntity<String> testProductNotification(@RequestBody ProductEvent event) {
        eventPublisher.publish(event);
        return ResponseEntity.ok("Product notification sent to all subscribers");
    }
}
