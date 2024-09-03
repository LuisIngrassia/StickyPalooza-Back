package com.g12.tpo.server.controllers.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g12.tpo.server.entity.Role;
import com.g12.tpo.server.service.interfaces.AuthService;
import com.g12.tpo.server.service.interfaces.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthService service;
        private final CartService cartService;

        @PostMapping("/register/user")
        public ResponseEntity<AuthResponse> registerUser(
                @RequestBody RegisterRequest request) {
            request.setRole(Role.USER);

            AuthResponse authResponse = service.register(request);

            cartService.createCart(authResponse.getUserId());

            AuthResponse responseWithoutUserId = AuthResponse.builder()
            .accessToken(authResponse.getAccessToken())
            .build();

            return ResponseEntity.ok(responseWithoutUserId);
        }
    
        @PostMapping("/register/admin")
        public ResponseEntity<AuthResponse> registerAdmin(
                @RequestBody RegisterRequest request) {
            request.setRole(Role.ADMIN);
            return ResponseEntity.ok(service.register(request));
        }

        @PostMapping("/authenticate")
        public ResponseEntity<AuthResponse> authenticate(
                @RequestBody AuthRequest request) {
            return ResponseEntity.ok(service.authenticate(request));
        }
}