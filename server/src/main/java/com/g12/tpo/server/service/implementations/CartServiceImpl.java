package com.g12.tpo.server.service.implementations;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.entity.Cart;
import com.g12.tpo.server.entity.CartProduct;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.CartProductRepository;
import com.g12.tpo.server.repository.CartRepository;
import com.g12.tpo.server.repository.ProductRepository;
import com.g12.tpo.server.repository.UserRepository;
import com.g12.tpo.server.service.interfaces.CartService;
import com.g12.tpo.server.service.interfaces.UserService;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.HashSet;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartProductRepository cartProductRepository;

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

    @Transactional
    public void addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(quantity);

        cartProductRepository.save(cartProduct);

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }
    
    @Override
    public void deleteCart(Long id) {
        Cart cart = getCartById(id);
        cart.getCartProducts().clear();
        cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        // Fetch the user by ID from the repository
        User user = userRepository.findById(userId).orElse(null);
        
        // If user doesn't exist, return null
        if (user == null) {
            return null;
        }
    
        // Get the cart from the user
        Cart cart = user.getCart();
    
        // Check if the cart belongs to the user
        if (cart == null || !cart.getUser().getId().equals(userId)) {
            return null;
        }
    
        // Ensure that the cart products are initialized (important for lazy loading)
        Hibernate.initialize(cart.getCartProducts());
    
        // Return the cart
        return cart;
    }
    
    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAllWithCartProducts();
    }    

}
