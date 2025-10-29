package com.delivery_api.Delivery.API.service;

import com.delivery_api.Delivery.API.entity.Product;
import com.delivery_api.Delivery.API.entity.Restaurant;
import com.delivery_api.Delivery.API.repository.ProductRepository;
import com.delivery_api.Delivery.API.repository.RestaurantRepository;
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

    @Autowired
    private RestaurantRepository restaurantRepository;

    /*
        Register new Product by Restaurant
     */
    public Product register(Product product) {
        validateProductData(product);
        validateRestaurant(product.getRestaurant());
        validatePrice(product.getPrice());

        product.setAvailable(true);

        return productRepository.save(product);
    }

    /*
        Register Product by Restaurant ID
     */
    public Product registerByRestaurantId(Product product, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));

        if (!restaurant.getActive()) {
            throw new IllegalArgumentException("Cannot add products to inactive restaurant");
        }

        product.setRestaurant(restaurant);
        return register(product);
    }

    @Transactional(readOnly = true)
    public List<Product> listAvailable() {
        return productRepository.findByAvailableTrue();
    }

    @Transactional(readOnly = true)
    public List<Product> listUnavailable() {
        return productRepository.findByAvailableFalse();
    }

    @Transactional(readOnly = true)
    public Optional<Product> searchById(Long id) {
        return productRepository.findById(id);
    }

    public Product update(Long id, Product productUpdated) {
        Product product = searchById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        validateProductData(productUpdated);
        validatePrice(productUpdated.getPrice());

        product.setName(productUpdated.getName());
        product.setDescription(productUpdated.getDescription());
        product.setCategory(productUpdated.getCategory());
        product.setPrice(productUpdated.getPrice());
        product.setAvailable(productUpdated.getAvailable());
        product.setRestaurant(productUpdated.getRestaurant());

        return productRepository.save(product);
    }

    /*
        Make Product unavailable (soft delete)
     */
    public void unavailableProduct(Long id) {
        Product product = searchById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        product.unavailable();
        productRepository.save(product);
    }

    /*
        Make Product available again
     */
    public Product makeAvailable(Long id) {
        Product product = searchById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        product.makeAvailable();
        return productRepository.save(product);
    }

    /*
        Update Product price with validation
     */
    public Product updatePrice(Long id, Double newPrice) {
        Product product = searchById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        validatePrice(newPrice);
        product.setPrice(newPrice);

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> searchByRestaurant(Restaurant restaurant) {
        return productRepository.findByRestaurant(restaurant);
    }

    @Transactional(readOnly = true)
    public List<Product> searchByRestaurantId(Long restaurantId) {
        return productRepository.findByRestaurantId(restaurantId);
    }

    @Transactional(readOnly = true)
    public List<Product> searchAvailableByRestaurantId(Long restaurantId) {
        return productRepository.findAvailableByRestaurantId(restaurantId);
    }

    @Transactional(readOnly = true)
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndAvailableTrue(name);
    }

    @Transactional(readOnly = true)
    public List<Product> searchByCategory(String category) {
        return productRepository.findByCategoryAndAvailableTrue(category);
    }

    @Transactional(readOnly = true)
    public List<Product> searchByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Price range values cannot be null");
        }
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Prices cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    /*
        Check if product is available
     */
    @Transactional(readOnly = true)
    public boolean isProductAvailable(Long id) {
        Product product = searchById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        return product.isAvailable();
    }

    /*
        Validate product data
     */
    private void validateProductData(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is empty");
        }

        if (product.getName().length() < 2) {
            throw new IllegalArgumentException("Product name should have at least 2 characters");
        }

        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Product category is required");
        }
    }

    /*
        Validate restaurant
     */
    private void validateRestaurant(Restaurant restaurant) {
        if (restaurant == null || restaurant.getId() == null) {
            throw new IllegalArgumentException("Restaurant is required");
        }

        Restaurant existingRestaurant = restaurantRepository.findById(restaurant.getId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurant.getId()));

        if (!existingRestaurant.getActive()) {
            throw new IllegalArgumentException("Cannot add products to inactive restaurant");
        }
    }

    /*
        Validate price
     */
    private void validatePrice(Double price) {
        if (price == null) {
            throw new IllegalArgumentException("Price is required");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (price > 10000) {
            throw new IllegalArgumentException("Price cannot exceed R$ 10,000.00");
        }

        // Check for valid decimal places (max 2)
        String priceStr = String.valueOf(price);
        if (priceStr.contains(".")) {
            String[] parts = priceStr.split("\\.");
            if (parts.length > 1 && parts[1].length() > 2) {
                throw new IllegalArgumentException("Price can have at most 2 decimal places");
            }
        }
    }
}
