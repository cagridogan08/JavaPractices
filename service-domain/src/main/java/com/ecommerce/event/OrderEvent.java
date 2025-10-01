package com.ecommerce.event;
import com.ecommerce.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrderEvent extends DomainEvent {
    private Long orderId;
    private String productId;
    private Integer quantity;
    private String status;
    private String customerId;

    public OrderEvent(Long orderId, String productId, Integer quantity,
                      String status, String customerId) {
        super("ORDER_EVENT", "order-service");
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
        this.customerId = customerId;
    }

    public OrderEvent(Order order, String eventType) {
        super( eventType, "order-service");
        this.orderId = order.getId();
        this.productId = order.getProductId();
        this.quantity = order.getQuantity();
        this.status = order.getStatus();
    }
}

