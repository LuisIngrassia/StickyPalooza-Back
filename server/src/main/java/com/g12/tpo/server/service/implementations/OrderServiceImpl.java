package com.g12.tpo.server.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.repository.OrderRepository;
import com.g12.tpo.server.service.interfaces.OrderService;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrder(Long id, Order orderDetails) {
        Order order = getOrderById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderDate(orderDetails.getOrderDate());
        order.setTotalPrice(orderDetails.getTotalPrice());
        // order.setProducts(orderDetails.getProducts()); // Descomentar si es necesario
        order.setUser(orderDetails.getUser());
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
