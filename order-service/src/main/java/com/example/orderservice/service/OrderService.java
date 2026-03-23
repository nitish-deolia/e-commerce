package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final String productServiceUrl;
    private final String inventoryServiceUrl;

    public OrderService(
            OrderRepository orderRepository,
            RestTemplate restTemplate,
            @Value("${services.product.url}") String productServiceUrl,
            @Value("${services.inventory.url}") String inventoryServiceUrl
    ) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
        this.inventoryServiceUrl = inventoryServiceUrl;
    }

    public Order createOrder(Order order) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            ProductDto product = restTemplate.getForObject(
                    productServiceUrl + "/products/" + item.getProductId(),
                    ProductDto.class
            );
            if (product == null) {
                throw new RuntimeException("Product not found: " + item.getProductId());
            }

            InventoryDto inventory = restTemplate.getForObject(
                    inventoryServiceUrl + "/inventory/" + item.getProductId(),
                    InventoryDto.class
            );
            if (inventory == null || inventory.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product " + item.getProductId());
            }

            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setOrder(order);
            total = total.add(item.getSubtotal());
        }

        for (OrderItem item : order.getItems()) {
            InventoryDto inventory = restTemplate.getForObject(
                    inventoryServiceUrl + "/inventory/" + item.getProductId(),
                    InventoryDto.class
            );
            int newStock = inventory.getStock() - item.getQuantity();
            restTemplate.put(
                    inventoryServiceUrl + "/inventory/" + item.getProductId() + "?stock=" + newStock,
                    null
            );
        }

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.CONFIRMED);
        order.setTotalPrice(total);

        return orderRepository.save(order);
    }
}
