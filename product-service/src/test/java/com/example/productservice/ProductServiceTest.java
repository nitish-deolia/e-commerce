package com.example.productservice;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setProductId(UUID.randomUUID());
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setProductId(UUID.randomUUID());
        product2.setName("Product 2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setProductId(productId);
        product.setName("Test Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(productId);

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setName("New Product");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);

        assertEquals("New Product", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() {
        UUID productId = UUID.randomUUID();
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setName("Old Name");

        Product updatedDetails = new Product();
        updatedDetails.setName("New Name");
        updatedDetails.setPrice(BigDecimal.TEN);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product result = productService.updateProduct(productId, updatedDetails);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testUpdateProductNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Product result = productService.updateProduct(productId, new Product());

        assertNull(result);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testDeleteProduct() {
        UUID productId = UUID.randomUUID();
        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }
}
