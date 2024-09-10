package com.g12.tpo.server.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.dto.OrderDTO;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.OrderProduct;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.OrderProductRepository;
import com.g12.tpo.server.repository.OrderRepository;
import com.g12.tpo.server.repository.ProductRepository;
import com.g12.tpo.server.service.interfaces.OrderService;
import com.g12.tpo.server.service.interfaces.UserService;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO) {
        
        User user = userService.getUserById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        BigDecimal totalAmount = BigDecimal.ZERO;

        Order order = Order.builder()
                .user(user)
                .orderDate(new Date())
                .orderProducts(new HashSet<>())
                .totalAmount(totalAmount)
                .build();
    
        for (Map.Entry<Long, Integer> entry : orderDTO.getProductQuantities().entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
    
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
    
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(quantity);
    
            orderProductRepository.save(orderProduct);
    
            productRepository.save(product);
    
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }
    
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }
    

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = orderProduct.getProduct();
            int quantity = orderProduct.getQuantity();
            product.setStockQuantity(product.getStockQuantity() + quantity);  // Devolver stock
            productRepository.save(product);
        }

        orderRepository.deleteById(id);
    }
}
