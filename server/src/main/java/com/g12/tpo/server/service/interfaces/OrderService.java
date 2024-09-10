package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.dto.OrderDTO;
import com.g12.tpo.server.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDTO orderDTO);
    Order getOrderById(Long id);
    List<Order> getAllOrders();
    void deleteOrder(Long id);
}
