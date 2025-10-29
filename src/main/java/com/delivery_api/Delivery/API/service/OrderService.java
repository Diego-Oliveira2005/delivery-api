package com.delivery_api.Delivery.API.service;

import com.delivery_api.Delivery.API.entity.Customer;
import com.delivery_api.Delivery.API.entity.Order;
import com.delivery_api.Delivery.API.entity.Product;
import com.delivery_api.Delivery.API.repository.OrderRepository;
import com.delivery_api.Delivery.API.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    /*
        Register new Order
     */
    public Order register(Order order) {
        validateOrderData(order);

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }

        if (order.getStatus() == null || order.getStatus().trim().isEmpty()) {
            order.setStatus("PENDING");
        }

        // Validate status value
        validateStatus(order.getStatus());

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Order> searchById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> searchByCustomer(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }

    @Transactional(readOnly = true)
    public List<Order> searchByStatus(String status) {
        validateStatus(status);
        return orderRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Order> searchByCustomerAndStatus(Customer customer, String status) {
        validateStatus(status);
        return orderRepository.findByCustomerAndStatus(customer, status);
    }

    @Transactional(readOnly = true)
    public List<Order> searchByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Order> searchByCustomerAndDateRange(Customer customer, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCustomerAndOrderDateBetween(customer, startDate, endDate);
    }

    public Order update(Long id, Order orderUpdated) {
        Order order = searchById(id).orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        validateOrderData(orderUpdated);
        validateStatus(orderUpdated.getStatus());

        order.setStatus(orderUpdated.getStatus());
        order.setTotalValue(orderUpdated.getTotalValue());
        order.setItems(orderUpdated.getItems());
        order.setCustomer(orderUpdated.getCustomer());
        order.setRestaurant(orderUpdated.getRestaurant());

        return orderRepository.save(order);
    }

    /*
        Calculate order total value based on product IDs
     */
    public Double calculateOrderTotal(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product list cannot be empty");
        }

        double total = 0.0;
        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
            
            if (!product.getAvailable()) {
                throw new IllegalArgumentException("Product not available: " + product.getName());
            }
            
            total += product.getPrice();
        }

        return total;
    }

    /*
        Calculate order total value and add restaurant delivery fee
     */
    public Double calculateOrderTotalWithDelivery(List<Long> productIds, Long restaurantId) {
        Double subtotal = calculateOrderTotal(productIds);
        
        Order tempOrder = new Order();
        tempOrder.setRestaurant(new com.delivery_api.Delivery.API.entity.Restaurant());
        tempOrder.getRestaurant().setId(restaurantId);
        
        // You can fetch the restaurant to get delivery fee
        // For now, just return subtotal
        return subtotal;
    }

    /*
        Change order status to CONFIRMED
     */
    public Order confirmOrder(Long id) {
        Order order = searchById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        
        order.confirm();
        return orderRepository.save(order);
    }

    /*
        Change order status to DELIVERED
     */
    public Order deliverOrder(Long id) {
        Order order = searchById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        
        order.deliver();
        return orderRepository.save(order);
    }

    /*
        Change order status to CANCELLED
     */
    public Order cancelOrder(Long id) {
        Order order = searchById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        
        order.cancel();
        return orderRepository.save(order);
    }

    /*
        Update order status manually
     */
    public Order updateStatus(Long id, String newStatus) {
        Order order = searchById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        
        validateStatus(newStatus);
        validateStatusTransition(order.getStatus(), newStatus);
        
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = searchById(id).orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        orderRepository.delete(order);
    }

    /*
        Validate order data
     */
    private void validateOrderData(Order order) {
        if (order.getOrderNumber() == null || order.getOrderNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Order number is empty");
        }

        if (order.getCustomer() == null) {
            throw new IllegalArgumentException("Customer is required");
        }

        if (order.getRestaurant() == null) {
            throw new IllegalArgumentException("Restaurant is required");
        }

        if (order.getTotalValue() == null || order.getTotalValue() <= 0) {
            throw new IllegalArgumentException("Total value must be greater than zero");
        }

        if (order.getItems() == null || order.getItems().trim().isEmpty()) {
            throw new IllegalArgumentException("Items list cannot be empty");
        }
    }

    /*
        Validate status value
     */
    private void validateStatus(String status) {
        List<String> validStatuses = Arrays.asList("PENDING", "CONFIRMED", "DELIVERED", "CANCELLED");
        
        if (status == null || !validStatuses.contains(status.toUpperCase())) {
            throw new IllegalArgumentException("Invalid status. Valid values: " + String.join(", ", validStatuses));
        }
    }

    /*
        Validate status transition
     */
    private void validateStatusTransition(String currentStatus, String newStatus) {
        currentStatus = currentStatus.toUpperCase();
        newStatus = newStatus.toUpperCase();

        if (currentStatus.equals(newStatus)) {
            return; // Same status, no transition needed
        }

        switch (currentStatus) {
            case "PENDING":
                if (!newStatus.equals("CONFIRMED") && !newStatus.equals("CANCELLED")) {
                    throw new IllegalArgumentException("Pending orders can only be confirmed or cancelled");
                }
                break;
            case "CONFIRMED":
                if (!newStatus.equals("DELIVERED") && !newStatus.equals("CANCELLED")) {
                    throw new IllegalArgumentException("Confirmed orders can only be delivered or cancelled");
                }
                break;
            case "DELIVERED":
                throw new IllegalArgumentException("Delivered orders cannot change status");
            case "CANCELLED":
                throw new IllegalArgumentException("Cancelled orders cannot change status");
        }
    }
}
