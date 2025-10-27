package com.delivery_api.Delivery.API.controller;

import com.delivery_api.Delivery.API.entity.Customer;
import com.delivery_api.Delivery.API.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
@CrossOrigin("*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /*
        Register new Customer
     */

    @PostMapping
    public ResponseEntity<?> registerCustomer(@Validated @RequestBody Customer customer) {
        try {
            Customer customerSaved = customerService.register(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerSaved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        List all active customers
     */

    @GetMapping
    public ResponseEntity<List<Customer>> listCustomers() {
        List<Customer> customers = customerService.listActives();
        return ResponseEntity.ok(customers);
    }

    /*
        Search customer by ID
     */

    @GetMapping("/{id}")
    public ResponseEntity<?> searchById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.searchById(id);

        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
        Update Customer
     */

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @Validated @RequestBody Customer customer) {
        try {
            Customer customerUpdated = customerService.update(id, customer);
            return ResponseEntity.ok(customerUpdated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Deactivate Customer (soft delete)
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deactivateCustomer(id);
            return ResponseEntity.ok().body("Customer deleted successfully");
        } catch(IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body("error: " + exception.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Search Customer by name
     */

    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchByName(@RequestParam String name) {
        List<Customer> customers = customerService.searchByName(name);
        return ResponseEntity.ok(customers);
    }

    /*
        Search Customer by email
     */

    @GetMapping("/email/{email}")
    public ResponseEntity<?> searchByEmail(@PathVariable String email) {
        Optional<Customer> customer = customerService.searchByEmail(email);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
