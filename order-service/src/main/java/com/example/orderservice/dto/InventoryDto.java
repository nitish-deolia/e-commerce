package com.example.orderservice.dto;

import java.util.UUID;

public class InventoryDto {
    private UUID productId;
    private int stock;

    public InventoryDto() {
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
