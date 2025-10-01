package com.ecommerce.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class InventoryEvent extends DomainEvent {
    private String productId;
    private Integer quantity;
    private Integer reservedQuantity;
    private String action; // RESERVED, RELEASED, ADDED, LOW_STOCK

    public InventoryEvent(String productId, Integer quantity,
                          Integer reservedQuantity, String action) {
        super("INVENTORY_EVENT", "inventory-service");
        this.productId = productId;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.action = action;
    }
}
