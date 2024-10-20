package com.g12.tpo.server.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.dto.OrderDTO;
import com.g12.tpo.server.entity.Cart;
import com.g12.tpo.server.entity.CartProduct;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.OrderProduct;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.CartRepository;
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
    private CartRepository cartRepository;

    @Transactional
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

            OrderProduct orderProduct = OrderProduct.builder()
                    .order(order)  // The order should be set before adding to the collection
                    .product(product)
                    .quantity(quantity)
                    .build();

            // Add to the order's set of orderProducts
            order.getOrderProducts().add(orderProduct);

            // Calculate total amount
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        // Set total amount and save the order
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order createOrderFromCart(Long cartId) {

        BigDecimal totalAmount = BigDecimal.ZERO;

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    
        Order order = Order.builder()
                .user(cart.getUser())
                .orderDate(new Date())
                .orderProducts(new HashSet<>())
                .totalAmount(BigDecimal.ZERO)
                .build();
    

        for (CartProduct cartProduct : cart.getCartProducts()) {

            Product product = cartProduct.getProduct();
            int quantity = cartProduct.getQuantity();

            BigDecimal productTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    
            OrderProduct orderProduct = OrderProduct.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .build();
    
            order.getOrderProducts().add(orderProduct);
    
            totalAmount = totalAmount.add(productTotalPrice);
        }
    
        order.setTotalAmount(totalAmount);
    
        Order savedOrder = orderRepository.save(order);

        cart.getCartProducts().clear();
        cartRepository.save(cart);
    
        return savedOrder;
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

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }


    @Transactional
    @Override
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);

        // Restore product stock
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = orderProduct.getProduct();
            int quantity = orderProduct.getQuantity();
            product.setStockQuantity(product.getStockQuantity() + quantity);
            productRepository.save(product);
        }

        // Delete the order
        orderRepository.deleteById(id);
    }
}
