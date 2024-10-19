package com.g12.tpo.server.service.implementations;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.controllers.auth.AuthRequest;
import com.g12.tpo.server.controllers.auth.AuthResponse;
import com.g12.tpo.server.controllers.auth.RegisterRequest;
import com.g12.tpo.server.controllers.config.JwtService;
import com.g12.tpo.server.entity.Cart;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.UserRepository;
import com.g12.tpo.server.service.interfaces.AuthService;
import com.g12.tpo.server.service.interfaces.CartService;
import com.g12.tpo.server.exceptions.InvalidCredentialsException; 
import com.g12.tpo.server.exceptions.UserNotFoundException;      

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CartService cartService;  // Inject CartService

    @Override
    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = repository.save(user);

        // Create a new cart for the user
        Cart cart = cartService.createCart(savedUser.getId());

        var jwtToken = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .userId(savedUser.getId())
                .role(savedUser.getRole().name())
                .cartId(cart.getId()) 
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        // Fetch user's cart
        Cart cart = cartService.getCartByIdForUser(user.getCart().getId(), user.getId());

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .accessToken(token)
                .userId(user.getId())
                .role(user.getRole().name())
                .cartId(cart.getId())
                .build();
    }
}
