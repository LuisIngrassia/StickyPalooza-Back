package com.g12.tpo.server.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.entity.User; // Asegúrate de tener la entidad User
import com.g12.tpo.server.repository.OrderRepository;
import com.g12.tpo.server.repository.BillRepository;
import com.g12.tpo.server.repository.UserRepository; // Asegúrate de tener el repositorio UserRepository
import com.g12.tpo.server.service.interfaces.OrderService;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository; // Asegúrate de tener el repositorio UserRepository

    @Override
    public Order createOrder(Order order) {
        
        if (order.getBill() != null) {
            Bill bill = billRepository.findById(order.getBill().getId())
                    .orElseThrow(() -> new RuntimeException("Bill not found"));
            order.setTotalAmount(bill.getTotalAmount());
        }
        // Buscar User por userId
        if (order.getUser() != null) {
            User user = userRepository.findById(order.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
        }
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
        order.setTotalAmount(orderDetails.getTotalAmount());
        // Actualizar user si está presente en los detalles
        if (orderDetails.getUser() != null) {
            User user = userRepository.findById(orderDetails.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
        }
        // Actualizar bill si está presente en los detalles
        if (orderDetails.getBill() != null) {
            Bill bill = billRepository.findById(orderDetails.getBill().getId())
                    .orElseThrow(() -> new RuntimeException("Bill not found"));
            order.setBill(bill);
        }
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
