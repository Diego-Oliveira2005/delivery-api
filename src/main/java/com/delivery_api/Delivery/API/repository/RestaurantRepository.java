package com.delivery_api.Delivery.API.repository;

import com.delivery_api.Delivery.API.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByActiveTrue();

    boolean existsByPhone(String phone);

    List<Restaurant> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    List<Restaurant> findByCategory(String category);

    @Query("SELECT r FROM Restaurant r WHERE r.rating BETWEEN :min AND :max")
    List<Restaurant> findByRatingBetween(@Param("min") Double min, @Param("max") Double max);
}
