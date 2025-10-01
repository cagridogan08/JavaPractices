package com.ecommerce.inventoryservice.service;

import com.ecommerce.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;


    @Transactional
    public boolean reserveInventory(String productId,Integer quantity){
        var inventory = inventoryRepository.findByProductId(productId)
                .orElse(null);

        if (inventory == null) {
            return false;
        }

        int available = inventory.getQuantity() - inventory.getReservedQuantity();
        if (available >= quantity) {
            inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
            inventoryRepository.save(inventory);
            return true;
        }

        return false;
    }
    public Inventory getInventory(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    public Inventory updateInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory create(Inventory inventory) {
        return inventoryRepository.saveAndFlush(inventory);
    }
}
