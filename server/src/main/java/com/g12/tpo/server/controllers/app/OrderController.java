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
import com.g12.tpo.server.dto.ProductDTO;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.OrderProduct;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.service.interfaces.OrderService;
import com.g12.tpo.server.repository.UserRepository;
import com.g12.tpo.server.repository.ProductRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
<<<<<<< HEAD
    private UserRepository userRepository;
=======
    private ProductRepository productRepository;
>>>>>>> cc9e352 (BILL TERMINADO (FALTA PROBAR), LUISPA HACE ORDER GRACIAS PORFA)

    private OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
            .id(order.getId())
            .orderDate(order.getOrderDate())
            .totalAmount(order.getTotalAmount())
            .userId(order.getUser() != null ? order.getUser().getId() : null)
            .build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO, @RequestBody List<ProductDTO> productDTOs) {
        // Obtener usuario
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Crear instancia de Order
        Order order = Order.builder()
                .id(orderDTO.getId())
                .orderDate(orderDTO.getOrderDate() != null ? orderDTO.getOrderDate() : new Date())
                .totalAmount(orderDTO.getTotalAmount())
                .user(user)
                .build();

        // Guardar orden
        Order createdOrder = orderService.createOrder(order);

        // Asociar productos a la orden
        List<OrderProduct> orderProducts = productDTOs.stream()
                .map(productDTO -> {
                    Product product = productRepository.findById(productDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    return OrderProduct.builder()
                            .order(createdOrder)
                            .product(product)
                            .quantity(productDTO.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        // Guardar los productos de la orden (esto se debe hacer en el servicio)
        orderService.saveOrderProducts(orderProducts);

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
