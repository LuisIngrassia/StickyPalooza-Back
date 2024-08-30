package com.g12.tpo.server.controllers.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g12.tpo.server.entity.Role;
import com.g12.tpo.server.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthService service;
    

        @PostMapping("/register/user")
        public ResponseEntity<AuthResponse> registerUser(
                @RequestBody RegisterRequest request) {
            request.setRole(Role.USER);
            return ResponseEntity.ok(service.register(request));
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