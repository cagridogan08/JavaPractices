package com.ecommerce.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProductEvent extends DomainEvent {
    private String productId;
    private String productName;
    private BigDecimal price;
    private String action; // CREATED, UPDATED, DELETED, PRICE_CHANGED

    public ProductEvent(String productId, String productName,
                        BigDecimal price, String action) {
        super("PRODUCT_EVENT", "product-service");
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.action = action;
    }
}
