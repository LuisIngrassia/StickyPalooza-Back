package com.g12.tpo.server.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.entity.Cart;
import com.g12.tpo.server.repository.CartRepository;
import com.g12.tpo.server.service.interfaces.CartService;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart createCart(Cart cart) {
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
