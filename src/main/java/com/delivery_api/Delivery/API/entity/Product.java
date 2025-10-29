package com.delivery_api.Delivery.API.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    private String category;

    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @PrePersist
    protected void onCreate() {
        if (available == null) {
            available = true;
        }
    }

    public void unavailable() {
        this.available = false;
    }

    public void makeAvailable() {
        this.available = true;
    }

    public boolean isAvailable() {
        return available != null && available;
    }

    public boolean isPriceValid() {
        return price != null && price > 0;
    }

    public boolean belongsToRestaurant(Long restaurantId) {
        return restaurant != null && restaurant.getId().equals(restaurantId);
    }
}
