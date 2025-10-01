package com.ecommerce.orderservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}

