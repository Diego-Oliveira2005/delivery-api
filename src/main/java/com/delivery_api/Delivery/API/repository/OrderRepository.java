package com.delivery_api.Delivery.API.repository;

import com.delivery_api.Delivery.API.entity.Customer;
import com.delivery_api.Delivery.API.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer(Customer customer);

    List<Order> findByStatus(String status);

    List<Order> findByCustomerAndStatus(Customer customer, String status);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.customer = :customer AND o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByCustomerAndOrderDateBetween(@Param("customer") Customer customer, 
                                                   @Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);
}
