package com.ecommerce.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class UpdateOrderRequest{
    @NotBlank(message = "Order ID is required")
    private Long orderId;
    @NotBlank (message = "Status  is required")
    private String status;
    
}
