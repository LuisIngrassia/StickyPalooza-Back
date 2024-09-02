package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.entity.Cart;
import java.util.List;

public interface CartService {
    Cart createCart(Cart cart);

    Cart getCartById(Long id);

    List<Cart> getAllCarts();

    Cart updateCart(Long id, Cart cartDetails);

    void deleteCart(Long id);
}
