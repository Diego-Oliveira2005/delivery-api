package com.delivery_api.Delivery.API.controller;

import com.delivery_api.Delivery.API.entity.Customer;
import com.delivery_api.Delivery.API.entity.Order;
import com.delivery_api.Delivery.API.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /*
        Register new Order
     */
    @PostMapping
    public ResponseEntity<?> registerOrder(@Validated @RequestBody Order order) {
        try {
            Order orderSaved = orderService.register(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderSaved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        List all Orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> listOrders() {
        List<Order> orders = orderService.listAll();
        return ResponseEntity.ok(orders);
    }

    /*
        Search Order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> searchById(@PathVariable Long id) {
        Optional<Order> order = orderService.searchById(id);

        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
        Update Order
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @Validated @RequestBody Order order) {
        try {
            Order orderUpdated = orderService.update(id, order);
            return ResponseEntity.ok(orderUpdated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Delete Order
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok().body("Order deleted successfully");
        } catch(IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body("error: " + exception.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Search Orders by Customer
     */
    @PostMapping("/customer")
    public ResponseEntity<List<Order>> searchByCustomer(@RequestBody Customer customer) {
        List<Order> orders = orderService.searchByCustomer(customer);
        return ResponseEntity.ok(orders);
    }

    /*
        Search Orders by Status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> searchByStatus(@PathVariable String status) {
        List<Order> orders = orderService.searchByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /*
        Search Orders by Customer and Status
     */
    @PostMapping("/customer/status/{status}")
    public ResponseEntity<List<Order>> searchByCustomerAndStatus(@RequestBody Customer customer, 
                                                                   @PathVariable String status) {
        List<Order> orders = orderService.searchByCustomerAndStatus(customer, status);
        return ResponseEntity.ok(orders);
    }

    /*
        Search Orders by Date Range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> searchByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Order> orders = orderService.searchByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /*
        Search Orders by Customer and Date Range
     */
    @PostMapping("/customer/date-range")
    public ResponseEntity<List<Order>> searchByCustomerAndDateRange(
            @RequestBody Customer customer,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Order> orders = orderService.searchByCustomerAndDateRange(customer, startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /*
        Calculate order total
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateTotal(@RequestBody List<Long> productIds) {
        try {
            Double total = orderService.calculateOrderTotal(productIds);
            return ResponseEntity.ok(java.util.Map.of("total", total));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Confirm Order (PENDING -> CONFIRMED)
     */
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<?> confirmOrder(@PathVariable Long id) {
        try {
            Order order = orderService.confirmOrder(id);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Deliver Order (CONFIRMED -> DELIVERED)
     */
    @PatchMapping("/{id}/deliver")
    public ResponseEntity<?> deliverOrder(@PathVariable Long id) {
        try {
            Order order = orderService.deliverOrder(id);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Cancel Order
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            Order order = orderService.cancelOrder(id);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /*
        Update Order Status manually
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Order order = orderService.updateStatus(id, status);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
