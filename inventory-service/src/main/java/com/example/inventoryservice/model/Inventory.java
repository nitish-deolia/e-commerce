package com.example.inventoryservice.model;

import jakarta.persistence.*;

@Entity
public class Inventory {
    @Id
    private Long productId;
    private Integer stock;

    // Getters and setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}