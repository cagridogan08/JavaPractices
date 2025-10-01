package com.ecommerce.inventoryservice.service;

import com.ecommerce.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;


    @Transactional
    public boolean reserveInventory(String productId, Integer quantity) {
        try {
            Inventory inventory = inventoryRepository.findByProductId(productId)
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            int available = inventory.getQuantity() - inventory.getReservedQuantity();
            if (available < quantity) {
                return false;
            }

            inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
            inventoryRepository.save(inventory);
            return true;
        } catch (OptimisticLockingFailureException e) {
            log.warn("Concurrent update detected, retrying...");
            return reserveInventory(productId, quantity); // Retry
        }
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
