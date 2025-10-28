package com.delivery_api.Delivery.API.service;

import com.delivery_api.Delivery.API.entity.*;
import com.delivery_api.Delivery.API.entity.Product;
import com.delivery_api.Delivery.API.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    /*
        Register new Product
     */

    public Product register(Product product) {

        validateProductData(product);

        product.setAvailable(true);

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> listAvailable() {
        return productRepository.findByAvailableTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Product> searchById(Long id) {
        return productRepository.findById(id);
    }

    public Product update(Long id, Product productUpdated) {
        Product product = searchById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        if (product.getAvailable() != null && !product.getAvailable()) {
            throw new IllegalArgumentException("Cannot update unavailable product");
        }

        validateProductData(productUpdated);
        

        product.setName(productUpdated.getName());
        product.setDescription(productUpdated.getDescription());
        product.setCategory(productUpdated.getCategory());
        product.setPrice(productUpdated.getPrice());
        product.setAvailable(productUpdated.getAvailable());
        product.setRestaurant(productUpdated.getRestaurant()); // ✅ ADICIONAR

        return productRepository.save(product);
    }

    public void unavailableProduct(Long id) {
        Product product = searchById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        product.unavailable();
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> searchByRestaurant(Restaurant restaurant) {
        return productRepository.findByRestaurant(restaurant);
    }

    @Transactional(readOnly = true)
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndAvailableTrue(name);
    }

    @Transactional(readOnly = true)
    public List<Product> searchByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    private void validateProductData(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is empty");
        }

        if (product.getName().length() < 2) {
            throw new IllegalArgumentException("Name should have at least 2 characters");
        }
    }
}
