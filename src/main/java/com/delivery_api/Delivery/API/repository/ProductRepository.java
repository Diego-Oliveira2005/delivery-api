package com.delivery_api.Delivery.API.repository;

import com.delivery_api.Delivery.API.entity.Product;
import com.delivery_api.Delivery.API.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCaseAndAvailableTrue(String name);

    List<Product> findByRestaurant(Restaurant restaurant);

    List<Product> findByCategory(String category);

    List<Product> findByAvailableTrue();
}
