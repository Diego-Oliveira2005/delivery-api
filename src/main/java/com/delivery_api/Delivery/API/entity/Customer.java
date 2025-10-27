package com.delivery_api.Delivery.API.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String address;

    @Column(name = "date_register")
    private LocalDateTime dateRegister;

    @Column(nullable = true)
    private Boolean active;

    @PrePersist
    protected void onCreate() {
        if (dateRegister == null) {
            dateRegister = LocalDateTime.now();
        }
        if (active == null) {
            active = true;
        }
    }

    public void deactivate() {
        this.active = false;
    }
}
