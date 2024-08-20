package com.g12.tpo.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.service.OrderService;

import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }
    
    // @GetMapping("/{id}")
    // public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
    //     return ResponseEntity.ok(OrderService.getOrderById(id));
    // }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok (orderService.getAllOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
    
}
