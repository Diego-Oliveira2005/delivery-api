package com.delivery_api.Delivery.API.controller;

import com.delivery_api.Delivery.API.entity.Product;
import com.delivery_api.Delivery.API.entity.Restaurant;
import com.delivery_api.Delivery.API.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    /*
        Register new Product
     */
    @PostMapping
    public ResponseEntity<?> registerProduct(@Validated @RequestBody Product product) {
        try {
            Product productSaved = productService.register(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Register Product by Restaurant ID
     */
    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> registerProductByRestaurant(@PathVariable Long restaurantId, 
                                                          @Validated @RequestBody Product product) {
        try {
            Product productSaved = productService.registerByRestaurantId(product, restaurantId);
            return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        List all available Products
     */
    @GetMapping
    public ResponseEntity<List<Product>> listProducts() {
        List<Product> products = productService.listAvailable();
        return ResponseEntity.ok(products);
    }

    /*
        List all unavailable Products
     */
    @GetMapping("/unavailable")
    public ResponseEntity<List<Product>> listUnavailableProducts() {
        List<Product> products = productService.listUnavailable();
        return ResponseEntity.ok(products);
    }

    /*
        Search Product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> searchById(@PathVariable Long id) {
        Optional<Product> product = productService.searchById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
        Check if Product is available
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<?> checkAvailability(@PathVariable Long id) {
        try {
            boolean isAvailable = productService.isProductAvailable(id);
            return ResponseEntity.ok(java.util.Map.of("id", id, "available", isAvailable));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }

    /*
        Update Product
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Validated @RequestBody Product product) {
        try {
            Product productUpdated = productService.update(id, product);
            return ResponseEntity.ok(productUpdated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Update Product price
     */
    @PatchMapping("/{id}/price")
    public ResponseEntity<?> updatePrice(@PathVariable Long id, @RequestParam Double price) {
        try {
            Product product = productService.updatePrice(id, price);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Make Product unavailable (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.unavailableProduct(id);
            return ResponseEntity.ok().body("Product deleted successfully");
        } catch(IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body("error: " + exception.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Make Product available again
     */
    @PatchMapping("/{id}/available")
    public ResponseEntity<?> makeProductAvailable(@PathVariable Long id) {
        try {
            Product product = productService.makeAvailable(id);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Search Product by name
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchByName(@RequestParam String name) {
        List<Product> product = productService.searchByName(name);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
        Search Products by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> searchByCategory(@PathVariable String category) {
        List<Product> products = productService.searchByCategory(category);
        return ResponseEntity.ok(products);
    }

    /*
        Search Products by Restaurant
     */
    @PostMapping("/restaurant")
    public ResponseEntity<List<Product>> searchByRestaurant(@RequestBody Restaurant restaurant) {
        List<Product> products = productService.searchByRestaurant(restaurant);
        return ResponseEntity.ok(products);
    }

    /*
        Search Products by Restaurant ID
     */
    @GetMapping("/restaurant/{restaurantId}/all")
    public ResponseEntity<List<Product>> searchByRestaurantId(@PathVariable Long restaurantId) {
        List<Product> products = productService.searchByRestaurantId(restaurantId);
        return ResponseEntity.ok(products);
    }

    /*
        Search Available Products by Restaurant ID
     */
    @GetMapping("/restaurant/{restaurantId}/available")
    public ResponseEntity<List<Product>> searchAvailableByRestaurantId(@PathVariable Long restaurantId) {
        List<Product> products = productService.searchAvailableByRestaurantId(restaurantId);
        return ResponseEntity.ok(products);
    }

    /*
        Search Products by Price Range
     */
    @GetMapping("/price-range")
    public ResponseEntity<?> searchByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        try {
            List<Product> products = productService.searchByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(products);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }
}

