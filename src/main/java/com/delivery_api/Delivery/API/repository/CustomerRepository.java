package com.delivery_api.Delivery.API.repository;

import com.delivery_api.Delivery.API.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Customer> findByActiveTrue();

    List<Customer> findByNameContainingIgnoreCase(String name);

}
