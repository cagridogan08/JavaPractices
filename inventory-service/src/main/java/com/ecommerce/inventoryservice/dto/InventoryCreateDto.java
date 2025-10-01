package com.ecommerce.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryCreateDto {

    private String productId;

    private Integer quantity;

}

