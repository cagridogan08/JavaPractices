package com.ecommerce.inventoryservice.controller;

import com.ecommerce.Inventory;
import com.ecommerce.inventoryservice.dto.InventoryCreateDto;
import com.ecommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@CrossOrigin("*")
public class InventoryController {
    private final InventoryService inventoryRepository;

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> findByProductId(@PathVariable String productId)
    {
        return ResponseEntity.ok(inventoryRepository.getInventory(productId));
    }
    @PostMapping("/update")
    public ResponseEntity<Inventory> updateInventory(@RequestBody Inventory inventory)
    {
        return ResponseEntity.ok(inventoryRepository.updateInventory(inventory));
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory)
    {
        return ResponseEntity.ok(inventoryRepository.create(inventory));
    }

}
