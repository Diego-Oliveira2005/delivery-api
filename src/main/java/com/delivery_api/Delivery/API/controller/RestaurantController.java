package com.delivery_api.Delivery.API.controller;

import com.delivery_api.Delivery.API.entity.Restaurant;
import com.delivery_api.Delivery.API.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin("*")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    /* 
        Register new Restaurant
     */

    @PostMapping
    public ResponseEntity<?> registerRestaurant(@Validated @RequestBody Restaurant restaurant) {
        try {
            Restaurant restaurantSaved = restaurantService.register(restaurant);
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurantSaved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
    
    /*
        List all online/open Restaurants
     */
    @GetMapping
    public ResponseEntity<List<Restaurant>> listRestaurants() {
        List<Restaurant> restaurants = restaurantService.listOnline();
        return ResponseEntity.ok(restaurants);
    }

    /*
        List all online Restaurants (explicit)
     */
    @GetMapping("/online")
    public ResponseEntity<List<Restaurant>> listOnlineRestaurants() {
        List<Restaurant> restaurants = restaurantService.listOnline();
        return ResponseEntity.ok(restaurants);
    }

    /*
        Search Restaurant by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> searchById(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.searchById(id);

        if (restaurant.isPresent()) {
            return ResponseEntity.ok(restaurant.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /*
        Update Restaurant
     */

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @Validated @RequestBody Restaurant restaurant) {
        try {
            Restaurant restaurantUpdated = restaurantService.update(id, restaurant);
            return ResponseEntity.ok(restaurantUpdated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Check if Restaurant is online
     */

    @GetMapping("/{id}/status")
    public ResponseEntity<?> checkStatus(@PathVariable Long id) {
        try {
            boolean isOnline = restaurantService.isRestaurantOnline(id);
            return ResponseEntity.ok(java.util.Map.of(
                    "id", id,
                    "online", isOnline,
                    "status", isOnline ? "OPEN" : "CLOSED"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }

    /*
        Set Restaurant offline/closed
     */
    @PatchMapping("/{id}/offline")
    public ResponseEntity<?> setRestaurantOffline(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.setOffline(id);
            return ResponseEntity.ok(restaurant);
        } catch(IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body("error: " + exception.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Set Restaurant online/open
     */
    @PatchMapping("/{id}/online")
    public ResponseEntity<?> setRestaurantOnline(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.setOnline(id);
            return ResponseEntity.ok(restaurant);
        } catch(IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body("error: " + exception.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Toggle Restaurant status (online ↔ offline)
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleRestaurantStatus(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.toggleStatus(id);
            return ResponseEntity.ok(restaurant);
        } catch(IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body("error: " + exception.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Search Restaurant by name
     */

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchByName(@RequestParam String name) {
        List<Restaurant> restaurants = restaurantService.searchByName(name);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Restaurant>> searchByCategory(@PathVariable String category) {
        List<Restaurant> restaurants = restaurantService.searchByCategory(category);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<?> searchByRating(@PathVariable Double rating) {
        List<Restaurant> restaurants = restaurantService.searchByRating(rating);
        return ResponseEntity.ok(restaurants);
    }
}
