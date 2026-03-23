package com.example.inventoryservice.controller;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{productId}")
    public Inventory getInventory(@PathVariable UUID productId) {
        return inventoryService.getInventory(productId);
    }

    @PutMapping("/{productId}")
    public Inventory updateStock(@PathVariable UUID productId, @RequestParam Integer stock) {
        return inventoryService.updateStock(productId, stock);
    }
}
