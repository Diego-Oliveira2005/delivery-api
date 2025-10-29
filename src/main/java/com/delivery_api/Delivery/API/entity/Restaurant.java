package com.delivery_api.Delivery.API.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private String address;

    private String phone;

    @Column(name = "delivery_fee")
    private Double deliveryFee;

    private Double rating;

    @Column(nullable = true)
    private Boolean active;

    @PrePersist
    protected void onCreate() {
        if (active == null) {
            active = true;
        }
    }

    public void goOffline() {
        this.active = false;
    }

    public void goOnline() {
        this.active = true;
    }

    public boolean isOnline() {
        return active != null && active;
    }

    public boolean isOffline() {
        return active == null || !active;
    }

}
