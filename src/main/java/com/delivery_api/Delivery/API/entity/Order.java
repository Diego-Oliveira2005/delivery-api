package com.delivery_api.Delivery.API.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    private String status;

    @Column(name = "total_value")
    private Double totalValue;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    private String items;

    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }

    public boolean isConfirmed() {
        return "CONFIRMED".equalsIgnoreCase(status);
    }

    public boolean isDelivered() {
        return "DELIVERED".equalsIgnoreCase(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }

    public void confirm() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending orders can be confirmed");
        }
        this.status = "CONFIRMED";
    }

    public void deliver() {
        if (!isConfirmed()) {
            throw new IllegalStateException("Only confirmed orders can be delivered");
        }
        this.status = "DELIVERED";
    }

    public void cancel() {
        if (isDelivered()) {
            throw new IllegalStateException("Delivered orders cannot be cancelled");
        }
        this.status = "CANCELLED";
    }
}
