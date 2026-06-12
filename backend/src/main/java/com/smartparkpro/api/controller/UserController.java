package com.smartparkpro.api.controller;

import com.smartparkpro.api.dto.ApiDtos.UserRequest;
import com.smartparkpro.api.dto.ApiDtos.UserResponse;
import com.smartparkpro.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    List<UserResponse> all() { return service.findAll(); }

    @GetMapping("/{id}")
    UserResponse one(@PathVariable UUID id) { return service.findById(id); }

    @PostMapping
    UserResponse create(@Valid @RequestBody UserRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    UserResponse update(@PathVariable UUID id, @Valid @RequestBody UserRequest request) { return service.update(id, request); }

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id) { service.delete(id); }
}
