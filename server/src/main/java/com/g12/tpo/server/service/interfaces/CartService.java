package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.entity.Cart;
import java.util.List;

public interface CartService {
    Cart createCart(Long userId);

    Cart getCartById(Long id);

    Cart getCartByUserId(Long userId);

    List<Cart> getAllCarts();

    void addProductToCart(Long cartId, Long productId, int quantity);

    void removeProductFromCart(Long cartId, Long productId);

    void deleteCart(Long id);
}
