package com.g12.tpo.server.controllers.app;

import com.g12.tpo.server.dto.AddProductToCartRequest;
import com.g12.tpo.server.dto.CartDTO;

import com.g12.tpo.server.service.interfaces.CartService;
import com.g12.tpo.server.entity.Cart;
import com.g12.tpo.server.entity.CartProduct;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/carts")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    private CartDTO convertToDTO(Cart cart) {
        Map<Long, Integer> productQuantities = cart.getCartProducts().stream()
            .collect(Collectors.toMap(
                cp -> cp.getProduct().getId(),
                CartProduct::getQuantity
            ));

        return CartDTO.builder()
            .id(cart.getId())
            .userId(cart.getUser() != null ? cart.getUser().getId() : null)
            .productQuantities(productQuantities)
            .build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) {
        Cart createdCart = cartService.createCart(cartDTO.getUserId());
        return ResponseEntity.ok(convertToDTO(createdCart));
    }
    
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(convertToDTO(cart));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        List<CartDTO> cartDTOs = carts.stream()
                                       .map(this::convertToDTO)
                                       .collect(Collectors.toList());
        return ResponseEntity.ok(cartDTOs);
    }

    @PostMapping("/addProduct")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> addProductToCart(@RequestBody AddProductToCartRequest request) {
        try {
            cartService.addProductToCart(request.getCartId(), request.getProductId(), request.getQuantity());
            return ResponseEntity.ok("Product added to cart");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
