package com.g12.tpo.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = getOrderById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderDate(orderDetails.getOrderDate());
        order.setTotalPrice(orderDetails.getTotalPrice());
        order.setProducts(orderDetails.getProducts()); // Make sure products are correctly managed
        order.setUser(orderDetails.getUser()); // Make sure user is correctly managed
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
