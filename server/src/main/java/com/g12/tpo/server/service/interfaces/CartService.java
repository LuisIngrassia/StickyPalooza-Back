package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.entity.Cart;
import java.util.List;

public interface CartService {
    Cart createCart(Long userId);

    Cart getCartById(Long id);

    List<Cart> getAllCarts();

    void addProductToCart(Long cartId, Long productId, int quantity);

    void deleteCart(Long id);
}
