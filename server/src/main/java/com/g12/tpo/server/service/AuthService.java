package com.g12.tpo.server.service;

// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.core.AuthenticationException;

import com.g12.tpo.server.controllers.auth.AuthRequest;
import com.g12.tpo.server.controllers.auth.AuthResponse;
import com.g12.tpo.server.controllers.auth.RegisterRequest;
import com.g12.tpo.server.controllers.config.JwtService;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        // private final AuthenticationManager AuthManager;

        public AuthResponse register(RegisterRequest request) {
                var user = User.builder()
                                .firstName(request.getFirstname())
                                .lastName(request.getLastname())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(request.getRole())
                                .build();

                repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                return AuthResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }

        // public AuthResponse authenticate(AuthRequest request) {

        //         AuthManager.authenticate(
        //                         new UsernamePasswordAuthenticationToken(
        //                                         request.getEmail(),
        //                                         request.getPassword()));

        //         var user = repository.findByEmail(request.getEmail())
        //                         .orElseThrow();

        //         var jwtToken = jwtService.generateToken(user);
        //         return AuthResponse.builder()
        //                         .accessToken(jwtToken)
        //                         .build();
        // }

        public AuthResponse authenticate(AuthRequest request) {
                User user = repository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found"));
        
                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                    throw new RuntimeException("Invalid credentials");
                }
        
                String token = jwtService.generateToken(user); // Generate JWT token
                return new AuthResponse(token);
            }

}