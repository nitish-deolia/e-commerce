package com.example.inventoryservice;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
    }

    @Test
    void testGetInventoryReturnsExistingInventory() {
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setStock(7);

        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventory));

        Inventory result = inventoryService.getInventory(productId);

        assertEquals(productId, result.getProductId());
        assertEquals(7, result.getStock());
    }

    @Test
    void testGetInventoryReturnsDefaultWhenMissing() {
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

        Inventory result = inventoryService.getInventory(productId);

        assertEquals(productId, result.getProductId());
        assertEquals(0, result.getStock());
        assertEquals(10, result.getReorderLevel());
        assertEquals("MAIN", result.getWarehouse());
    }

    @Test
    void testUpdateStockUpdatesExistingInventory() {
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setStock(3);

        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Inventory result = inventoryService.updateStock(productId, 9);

        assertEquals(productId, result.getProductId());
        assertEquals(9, result.getStock());
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void testUpdateStockCreatesInventoryWhenMissing() {
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Inventory result = inventoryService.updateStock(productId, 4);

        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals(4, result.getStock());
        verify(inventoryRepository).save(any(Inventory.class));
    }
}
