package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product p = product.get();
            p.setName(productDetails.getName());
            p.setDescription(productDetails.getDescription());
            p.setPrice(productDetails.getPrice());
            p.setCategory(productDetails.getCategory());
            p.setStock(productDetails.getStock());
            return productRepository.save(p);
        }
        return null;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}