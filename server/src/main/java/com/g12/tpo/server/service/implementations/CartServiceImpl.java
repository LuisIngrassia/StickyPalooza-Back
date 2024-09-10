package com.g12.tpo.server.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.entity.Cart;
import com.g12.tpo.server.entity.CartProduct;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.CartRepository;
import com.g12.tpo.server.repository.ProductRepository;
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

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart createCart(Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        Cart cart = Cart.builder()
                .user(user)
                .cartProducts(new HashSet<>())
                .build();
    
        return cartRepository.save(cart);
    }
    
    @Override
    public Cart updateCart(Long id, Cart cartDetails) {
        Cart cart = getCartById(id);

        for (CartProduct cartProduct : cartDetails.getCartProducts()) {
            Product product = productRepository.findById(cartProduct.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + cartProduct.getProduct().getId()));
            int quantity = cartProduct.getQuantity();

            if (product.getStockQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName() + ". Available stock: " + product.getStockQuantity());
            }

            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
        }

        cart.setCartProducts(cartDetails.getCartProducts());
        cart.setUser(cartDetails.getUser());
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Long id) {
        Cart cart = getCartById(id);
    
        for (CartProduct cartProduct : cart.getCartProducts()) {
            Product product = cartProduct.getProduct();
            int quantity = cartProduct.getQuantity();
            product.setStockQuantity(product.getStockQuantity() + quantity);  
        }
    
        cartRepository.deleteById(id);  
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
    
}
