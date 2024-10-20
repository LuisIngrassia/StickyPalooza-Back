package com.g12.tpo.server.controllers.app;

import com.g12.tpo.server.dto.OrderDTO;
import com.g12.tpo.server.dto.OrderProductDTO;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.OrderProduct;
import com.g12.tpo.server.service.interfaces.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private OrderProductDTO convertToOrderProductDTO(OrderProduct orderProduct) {
        return OrderProductDTO.builder()
            .id(orderProduct.getId())
            .orderId(orderProduct.getOrder().getId())
            .productId(orderProduct.getProduct().getId())
            .quantity(orderProduct.getQuantity())
            .productPrice(orderProduct.getProduct().getPrice())
            .productName(orderProduct.getProduct().getName())
            .productImage(orderProduct.getProduct().getImage())
            .build();
    }
    
    private OrderDTO convertToDTO(Order order) {
        List<OrderProductDTO> orderProducts = order.getOrderProducts().stream()
            .map(this::convertToOrderProductDTO)
            .collect(Collectors.toList());
    
        return OrderDTO.builder()
            .id(order.getId())
            .userId(order.getUser() != null ? order.getUser().getId() : null)
            .orderDate(order.getOrderDate())
            .orderProducts(orderProducts)  // Similar to cartProducts in CartDTO
            .totalAmount(order.getTotalAmount())
            .build();
    }
    

    @PostMapping("/fromCart/{cartId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<OrderDTO> createOrderFromCart(@PathVariable Long cartId) {
        Order createdOrder = orderService.createOrderFromCart(cartId);
        return ResponseEntity.ok(convertToDTO(createdOrder));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(convertToDTO(order));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO> orderDTOs = orders.stream()
                                       .map(this::convertToDTO)
                                       .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/ordersFromUser/{userId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        List<OrderDTO> orderDTOs = orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
