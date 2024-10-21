package com.g12.tpo.server.controllers.app;

import com.g12.tpo.server.dto.AddProductToCartRequest;
import com.g12.tpo.server.dto.CartDTO;
import com.g12.tpo.server.dto.CartProductDTO;
import com.g12.tpo.server.service.interfaces.CartService;
import jakarta.persistence.EntityNotFoundException;
import com.g12.tpo.server.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carts")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    @Autowired
    private CartService cartService;

    private CartDTO convertToDTO(Cart cart) {
        List<CartProductDTO> cartProducts = cart.getCartProducts().stream()
            .map(cp -> CartProductDTO.builder()
                .id(cp.getId())
                .cartId(cart.getId())
                .productId(cp.getProduct().getId())
                .quantity(cp.getQuantity())
                .productPrice(cp.getProduct().getPrice())
                .productName(cp.getProduct().getName())
                .productImage(cp.getProduct().getImage())
                .build())
            .collect(Collectors.toList());

        return CartDTO.builder()
            .id(cart.getId())
            .userId(cart.getUser() != null ? cart.getUser().getId() : null)
            .cartProducts(cartProducts)
            .build();
    }


    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
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
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<String> addProductToCart(@RequestBody AddProductToCartRequest request) {
        try {
            cartService.addProductToCart(request.getCartId(), request.getProductId(), request.getQuantity());
            return ResponseEntity.ok("Product added to cart");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{cartId}/{productId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        try {
            cartService.removeProductFromCart(cartId, productId);
            return ResponseEntity.ok("Product removed from cart");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
