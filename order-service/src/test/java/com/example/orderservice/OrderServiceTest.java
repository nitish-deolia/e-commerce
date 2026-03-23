package com.example.orderservice;

import com.example.orderservice.dto.InventoryDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private FakeRestTemplate restTemplate;
    private OrderService orderService;

    private final String productServiceUrl = "http://localhost:8082";
    private final String inventoryServiceUrl = "http://localhost:8084";

    @BeforeEach
    void setUp() {
        restTemplate = new FakeRestTemplate();
        orderService = new OrderService(orderRepository, restTemplate, productServiceUrl, inventoryServiceUrl);
    }

    @Test
    void testCreateOrderSuccess() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Order order = new Order();
        order.setUserId(userId);

        OrderItem item = new OrderItem();
        item.setProductId(productId);
        item.setQuantity(2);
        order.setItems(List.of(item));

        ProductDto product = new ProductDto();
        product.setProductId(productId);
        product.setName("iPhone 15");
        product.setPrice(new BigDecimal("999.99"));

        InventoryDto inventory = new InventoryDto();
        inventory.setProductId(productId);
        inventory.setStock(5);

        restTemplate.stub(productServiceUrl + "/products/" + productId, product);
        restTemplate.stub(inventoryServiceUrl + "/inventory/" + productId, inventory);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order savedOrder = orderService.createOrder(order);

        assertEquals(Order.Status.CONFIRMED, savedOrder.getStatus());
        assertEquals(new BigDecimal("1999.98"), savedOrder.getTotalPrice());
        assertNotNull(savedOrder.getOrderDate());
        assertEquals(new BigDecimal("999.99"), savedOrder.getItems().get(0).getUnitPrice());
        assertEquals(new BigDecimal("1999.98"), savedOrder.getItems().get(0).getSubtotal());
        assertSame(savedOrder, savedOrder.getItems().get(0).getOrder());
        assertEquals(inventoryServiceUrl + "/inventory/" + productId + "?stock=3", restTemplate.getLastPutUrl());

        verify(orderRepository).save(order);
    }

    @Test
    void testCreateOrderThrowsWhenInventoryInsufficient() {
        UUID productId = UUID.randomUUID();

        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setProductId(productId);
        item.setQuantity(3);
        order.setItems(List.of(item));

        ProductDto product = new ProductDto();
        product.setProductId(productId);
        product.setPrice(new BigDecimal("49.99"));

        InventoryDto inventory = new InventoryDto();
        inventory.setProductId(productId);
        inventory.setStock(1);

        restTemplate.stub(productServiceUrl + "/products/" + productId, product);
        restTemplate.stub(inventoryServiceUrl + "/inventory/" + productId, inventory);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(order));

        assertEquals("Insufficient stock for product " + productId, exception.getMessage());
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void testCreateOrderThrowsWhenProductMissing() {
        UUID productId = UUID.randomUUID();

        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setProductId(productId);
        item.setQuantity(1);
        order.setItems(List.of(item));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(order));

        assertEquals("Product not found: " + productId, exception.getMessage());
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    private static class FakeRestTemplate extends RestTemplate {
        private final Map<String, Object> responses = new HashMap<>();
        private String lastPutUrl;

        void stub(String url, Object response) {
            responses.put(url, response);
        }

        String getLastPutUrl() {
            return lastPutUrl;
        }

        @Override
        public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) {
            return responseType.cast(responses.get(url));
        }

        @Override
        public void put(String url, Object request, Object... uriVariables) {
            this.lastPutUrl = url;
        }
    }
}
