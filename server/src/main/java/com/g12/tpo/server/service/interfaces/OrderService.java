package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(Order order);

    Optional<Order> getOrderById(Long id);

    List<Order> getAllOrders();

    Order updateOrder(Long id, Order orderDetails);

    void deleteOrder(Long id);
}
