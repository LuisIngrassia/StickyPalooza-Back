package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.dto.OrderDTO;
import com.g12.tpo.server.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDTO orderDTO);
    Order createOrderFromCart(Long cartId);
    Order getOrderById(Long id);
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(Long userId);
    void deleteOrder(Long id);
}
