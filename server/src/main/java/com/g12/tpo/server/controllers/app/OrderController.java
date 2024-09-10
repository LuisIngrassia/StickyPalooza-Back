package com.g12.tpo.server.controllers.app;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.g12.tpo.server.dto.OrderDTO;
import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.service.interfaces.BillService;
import com.g12.tpo.server.service.interfaces.OrderService;
import com.g12.tpo.server.repository.UserRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BillService billService;

    @Autowired
    private UserRepository userRepository; // Asegúrate de inyectar UserRepository

    private OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
            .id(order.getId())
            .orderDate(order.getOrderDate())
            .totalAmount(order.getTotalAmount())
            .userId(order.getUser() != null ? order.getUser().getId() : null)
            .billId(order.getBill() != null ? order.getBill().getId() : null)
            .build();
    }
    
    // private Order convertToEntity(OrderDTO dto) {
    //     Order order = new Order();
    //     order.setId(dto.getId());
    //     order.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : new Date()); // Usa la fecha proporcionada o la fecha actual
    //     return order;
    // }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        Bill bill = billService.getBillById(orderDTO.getBillId());
    
        if (bill == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        // Usando el patrón Builder para crear la instancia de Order
        Order order = Order.builder()
                .id(orderDTO.getId())
                .orderDate(orderDTO.getOrderDate() != null ? orderDTO.getOrderDate() : new Date())
                .totalAmount(bill.getTotalAmount())
                .bill(bill)
                .user(user)
                .build();
    
        Order createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(convertToDTO(createdOrder), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(convertToDTO(order.get()));
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO> orderDTOs = orders.stream()
                                        .map(this::convertToDTO)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
