package com.g12.tpo.server.controllers.app;

import com.g12.tpo.server.dto.OrderDTO;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.OrderProduct;
import com.g12.tpo.server.service.interfaces.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private OrderDTO convertToDTO(Order order) {
        Map<Long, Integer> productQuantities = order.getOrderProducts().stream()
            .collect(Collectors.toMap(
                op -> op.getProduct().getId(),
                OrderProduct::getQuantity
            ));

        return OrderDTO.builder()
            .userId(order.getUser() != null ? order.getUser().getId() : null)
            .orderDate(order.getOrderDate())
            .productQuantities(productQuantities)
            .build();
    }

    @PostMapping("/fromCart/{cartId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
