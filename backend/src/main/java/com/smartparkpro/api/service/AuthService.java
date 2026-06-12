package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.AuthDtos.AuthResponse;
import com.smartparkpro.api.dto.AuthDtos.LoginRequest;
import com.smartparkpro.api.dto.AuthDtos.RegisterRequest;
import com.smartparkpro.api.entity.User;
import com.smartparkpro.api.enums.Role;
import com.smartparkpro.api.exception.ApiException;
import com.smartparkpro.api.repository.UserRepository;
import com.smartparkpro.api.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository users, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.users = users;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email already registered");
        }
        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(encoder.encode(request.password()));
        user.setRole(request.role() == null ? Role.CUSTOMER : request.role());
        User saved = users.save(user);
        return response(saved);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().toLowerCase();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));
        return response(users.findByEmail(email).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials")));
    }

    private AuthResponse response(User user) {
        return new AuthResponse(jwtService.generate(user), user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }
}
