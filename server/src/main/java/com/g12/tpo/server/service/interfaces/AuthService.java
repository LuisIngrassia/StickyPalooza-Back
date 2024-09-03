package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.controllers.auth.AuthRequest;
import com.g12.tpo.server.controllers.auth.AuthResponse;
import com.g12.tpo.server.controllers.auth.LoginRequest;
import com.g12.tpo.server.controllers.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse authenticate(AuthRequest request);

    AuthResponse login(LoginRequest request);
}
