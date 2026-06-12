package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.UserRequest;
import com.smartparkpro.api.dto.ApiDtos.UserResponse;
import com.smartparkpro.api.entity.User;
import com.smartparkpro.api.exception.ApiException;
import com.smartparkpro.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final MapperService mapper;

    public UserService(UserRepository users, PasswordEncoder encoder, MapperService mapper) {
        this.users = users;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    public List<UserResponse> findAll() {
        return users.findAll().stream().map(mapper::toUser).toList();
    }

    public UserResponse findById(UUID id) {
        return mapper.toUser(get(id));
    }

    public UserResponse create(UserRequest request) {
        if (users.existsByEmail(request.email())) throw new ApiException(HttpStatus.CONFLICT, "Email already exists");
        User user = new User();
        apply(user, request);
        user.setPasswordHash(encoder.encode(request.password() == null ? "ChangeMe123!" : request.password()));
        return mapper.toUser(users.save(user));
    }

    public UserResponse update(UUID id, UserRequest request) {
        User user = get(id);
        apply(user, request);
        if (request.password() != null && !request.password().isBlank()) user.setPasswordHash(encoder.encode(request.password()));
        return mapper.toUser(users.save(user));
    }

    public void delete(UUID id) {
        users.delete(get(id));
    }

    User get(UUID id) {
        return users.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void apply(User user, UserRequest request) {
        user.setFullName(request.fullName());
        user.setEmail(request.email().toLowerCase());
        user.setRole(request.role());
        user.setEnabled(request.enabled());
    }
}
