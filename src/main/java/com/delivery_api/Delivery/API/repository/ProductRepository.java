package com.delivery_api.Delivery.API.repository;

import com.delivery_api.Delivery.API.entity.Product;
import com.delivery_api.Delivery.API.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCaseAndAvailableTrue(String name);

    List<Product> findByRestaurant(Restaurant restaurant);
    
    List<Product> findByRestaurantAndAvailableTrue(Restaurant restaurant);

    List<Product> findByCategory(String category);
    
    List<Product> findByCategoryAndAvailableTrue(String category);

    List<Product> findByAvailableTrue();
    
    List<Product> findByAvailableFalse();

    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId")
    List<Product> findByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.available = true")
    List<Product> findAvailableByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.available = true")
    List<Product> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
}
