package com.example.inventoryservice.service;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public Inventory getInventory(Long productId) {
        return inventoryRepository.findById(productId).orElse(new Inventory());
    }

    public Inventory updateStock(Long productId, Integer stock) {
        Inventory inventory = inventoryRepository.findById(productId).orElse(new Inventory());
        inventory.setProductId(productId);
        inventory.setStock(stock);
        return inventoryRepository.save(inventory);
    }
}