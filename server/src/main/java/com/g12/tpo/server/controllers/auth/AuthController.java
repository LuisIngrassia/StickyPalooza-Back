package com.g12.tpo.server.controllers.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.g12.tpo.server.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    
        private final AuthService service;
    
        @PostMapping("/register")
        public ResponseEntity<AuthResponse> register(
                @RequestBody RegisterRequest request) {
            return ResponseEntity.ok(service.register(request));
        }
    
        @PostMapping("/authenticate")
        public ResponseEntity<AuthResponse> authenticate(
                @RequestBody AuthRequest request) {
            return ResponseEntity.ok(service.authenticate(request));
        }
    }