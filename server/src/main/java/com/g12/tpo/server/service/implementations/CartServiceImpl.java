package com.g12.tpo.server.service.implementations;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.entity.Cart;
import com.g12.tpo.server.entity.CartProduct;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.CartRepository;
import com.g12.tpo.server.repository.ProductRepository;
import com.g12.tpo.server.repository.UserRepository;
import com.g12.tpo.server.service.interfaces.CartService;
import com.g12.tpo.server.service.interfaces.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
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
        // Fetch the cart along with its products
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    
        // Check for existing CartProduct
        Optional<CartProduct> existingCartProductOpt = cart.getCartProducts().stream()
                .filter(cp -> cp.getProduct().getId().equals(productId))
                .findFirst();
    
        if (existingCartProductOpt.isPresent()) {

            CartProduct cartProduct = existingCartProductOpt.get();
            int newQuantity = cartProduct.getQuantity() + quantity;
    
            Product product = cartProduct.getProduct();
            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Not enough stock for Product ID: " + productId);
            }

            if (newQuantity  < 1){
                throw new RuntimeException("Product Quantity cant be less than 1. Remove the product if needed.");
            }

            cartProduct.setQuantity(newQuantity);
        } else {
            // If it doesn't exist, fetch the product
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
                    
            if (quantity  < 1){
                throw new RuntimeException("Product Quantity cant be less than 1. Remove the product if needed.");
            }

            // Check stock availability for the new product
            if (product.getStockQuantity() < quantity) {
                throw new RuntimeException("Not enough stock for Product ID: " + productId);
            }
    
            // Create new CartProduct and add it to the cart's product set
            CartProduct newCartProduct = CartProduct.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
    
            cart.getCartProducts().add(newCartProduct);
        }
    
        // Update stock in the database
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        product.setStockQuantity(product.getStockQuantity() - quantity); // Subtract the quantity
        productRepository.save(product); // Save the updated product
    
        // Save the updated cart (optional, since the cart is already managed)
        cartRepository.save(cart); 
    }    
    
    @Override
    public void deleteCart(Long id) {
        Cart cart = getCartById(id);
        
        for (CartProduct cartProduct : cart.getCartProducts()) {
            Product product = cartProduct.getProduct();
            product.setStockQuantity(product.getStockQuantity() + cartProduct.getQuantity());
            productRepository.save(product); 
        }

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

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        Cart cart = user.getCart();

        if (cart == null || !cart.getUser().getId().equals(userId)) {
            return null;
        }

        Hibernate.initialize(cart.getCartProducts());

        return cart;
    }
    
    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAllWithCartProducts();
    }    

}
