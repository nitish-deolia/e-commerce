package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String PRODUCT_SERVICE_URL = "http://localhost:8082/products/";
    private static final String INVENTORY_SERVICE_URL = "http://localhost:8084/inventory/";

    public Order createOrder(Order order) {
        // Check inventory and get prices for each item
        for (OrderItem item : order.getItems()) {
            // Get product details
            ProductDto product = restTemplate.getForObject(PRODUCT_SERVICE_URL + item.getProductId(), ProductDto.class);
            if (product == null) {
                throw new RuntimeException("Product not found: " + item.getProductId());
            }

            // Check inventory
            InventoryDto inventory = restTemplate.getForObject(INVENTORY_SERVICE_URL + item.getProductId(), InventoryDto.class);
            if (inventory == null || inventory.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product " + item.getProductId());
            }

            // Set price in item (assuming OrderItem has price field, but it doesn't, so calculate total separately)
        }

        // Update inventory
        for (OrderItem item : order.getItems()) {
            InventoryDto inventory = restTemplate.getForObject(INVENTORY_SERVICE_URL + item.getProductId(), InventoryDto.class);
            int newStock = inventory.getStock() - item.getQuantity();
            restTemplate.put(INVENTORY_SERVICE_URL + item.getProductId() + "?stock=" + newStock, null);
        }

        // Calculate total
        double total = 0.0;
        for (OrderItem item : order.getItems()) {
            ProductDto product = restTemplate.getForObject(PRODUCT_SERVICE_URL + item.getProductId(), ProductDto.class);
            total += product.getPrice() * item.getQuantity();
        }
        order.setTotal(total);

        return orderRepository.save(order);
    }
}