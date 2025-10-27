package com.delivery_api.Delivery.API.service;

import com.delivery_api.Delivery.API.entity.Customer;
import com.delivery_api.Delivery.API.repository.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /*
        Register new Customer
     */

    public Customer register(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        validateCustomerData(customer);

        customer.setActive(true);

        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public List<Customer> listActives() {
        return customerRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Customer> searchById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer update(Long id, Customer customerUpdated) {
        Customer customer = searchById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));

        if (customer.getActive() != null && !customer.getActive()) {
            throw new IllegalArgumentException("Cannot update inactive customer");
        }

        validateCustomerData(customerUpdated);

        if (!customer.getEmail().equals(customerUpdated.getEmail())) {
            if (customerRepository.existsByEmail(customerUpdated.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " +  customerUpdated.getEmail());
            }
        }

        customer.setName(customerUpdated.getName());
        customer.setEmail(customerUpdated.getEmail());
        customer.setPhone(customerUpdated.getPhone());
        customer.setAddress(customerUpdated.getAddress());

        return customerRepository.save(customer);
    }

    public void deactivateCustomer(Long id) {
        Customer customer = searchById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));

        customer.deactivate();
        customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public List<Customer> searchByName(String name) {
        return customerRepository.findByNameContainingIgnoreCaseAndActiveTrue(name);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> searchByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    private void validateCustomerData(Customer customer) {
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is empty");
        }

        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is empty");
        }

        if (customer.getName().length() < 2) {
            throw new IllegalArgumentException("Name should have at least 2 characters");
        }
    }
}
