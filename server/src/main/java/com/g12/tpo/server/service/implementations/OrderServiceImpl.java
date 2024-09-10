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

    @Autowired
    private CartRepository cartRepository;  // Agregamos el CartRepository para acceder al carrito

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

    @Transactional
    @Override
    public Order createOrderFromCart(Long cartId) {
        
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Crear la orden para el usuario del carrito
        Order order = Order.builder()
                .user(cart.getUser())
                .orderDate(new Date())
                .orderProducts(new HashSet<>()) // Inicialmente vacía
                .totalAmount(BigDecimal.ZERO) // Inicializamos el total
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Procesar los productos del carrito
        for (CartProduct cartProduct : cart.getCartProducts()) {
            Product product = cartProduct.getProduct();
            int quantity = cartProduct.getQuantity();
            BigDecimal productTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

            // Crear OrderProduct basado en el CartProduct
            OrderProduct orderProduct = OrderProduct.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .build();

            // Guardar el OrderProduct
            orderProductRepository.save(orderProduct);
            order.getOrderProducts().add(orderProduct);

            // Sumar el precio del producto al total de la orden
            totalAmount = totalAmount.add(productTotalPrice);
        }

        // Actualizar el total en la orden
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        // Limpiar los productos del carrito (pero no eliminar el carrito)
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

    @Transactional
    @Override
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);

        // Restaurar el stock de los productos de la orden
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = orderProduct.getProduct();
            int quantity = orderProduct.getQuantity();
            product.setStockQuantity(product.getStockQuantity() + quantity);  // Devolver el stock
            productRepository.save(product);
        }

        // Eliminar la orden
        orderRepository.deleteById(id);
    }
}
