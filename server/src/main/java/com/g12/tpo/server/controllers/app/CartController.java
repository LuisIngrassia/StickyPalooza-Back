package com.g12.tpo.server.controllers.app;

import com.g12.tpo.server.dto.CartDTO;
import com.g12.tpo.server.entity.Cart;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) {
        Cart cart = convertToEntity(cartDTO);
        Cart createdCart = cartService.createCart(cart);
        return ResponseEntity.ok(convertToDTO(createdCart));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long id) {
        Cart cart = cartService.getCartById(id);
        return ResponseEntity.ok(convertToDTO(cart));
    }

    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        List<CartDTO> cartDTOs = carts.stream()
                                       .map(this::convertToDTO)
                                       .collect(Collectors.toList());
        return ResponseEntity.ok(cartDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable Long id, @RequestBody CartDTO cartDTO) {
        Cart cart = convertToEntity(cartDTO);
        Cart updatedCart = cartService.updateCart(id, cart);
        return ResponseEntity.ok(convertToDTO(updatedCart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setProductIds(cart.getProducts().stream()
                                .map(Product::getId)
                                .collect(Collectors.toSet()));
        return dto;
    }

    private Cart convertToEntity(CartDTO dto) {
        Cart cart = new Cart();
        cart.setId(dto.getId());

        User user = new User();
        user.setId(dto.getUserId());
        cart.setUser(user);

        Set<Product> products = dto.getProductIds().stream()
                                   .map(productId -> {
                                       Product product = new Product();
                                       product.setId(productId);
                                       return product;
                                   })
                                   .collect(Collectors.toSet());
        cart.setProducts(products);

        return cart;
    }
}
