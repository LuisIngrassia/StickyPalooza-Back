package com.g12.tpo.server.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.entity.Cart;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.CartRepository;
import com.g12.tpo.server.service.interfaces.CartService;
import com.g12.tpo.server.service.interfaces.UserService;

import java.util.List;
import java.util.HashSet;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Override
    public Cart createCart(Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = Cart.builder()
                .user(user)
                .products(new HashSet<>())
                .build();

        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Cart updateCart(Long id, Cart cartDetails) {
        Cart cart = getCartById(id);
        cart.setProducts(cartDetails.getProducts());
        cart.setUser(cartDetails.getUser());
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }
}
