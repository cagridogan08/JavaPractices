package com.ecommerce;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inventory")
@Data

public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private Integer quantity;
    private Integer reservedQuantity;
}
