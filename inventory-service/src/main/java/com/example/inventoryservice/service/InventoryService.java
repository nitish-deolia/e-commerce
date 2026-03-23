package com.example.inventoryservice.service;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public Inventory getInventory(UUID productId) {
        return inventoryRepository.findByProductId(productId).orElseGet(() -> {
            Inventory inventory = new Inventory();
            inventory.setProductId(productId);
            inventory.setStock(0);
            return inventory;
        });
    }

    public Inventory updateStock(UUID productId, Integer stock) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseGet(Inventory::new);
        inventory.setProductId(productId);
        inventory.setStock(stock);
        return inventoryRepository.save(inventory);
    }
}
