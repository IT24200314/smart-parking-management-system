package com.smartparkpro.api.controller;

import com.smartparkpro.api.dto.ApiDtos.PaymentRequest;
import com.smartparkpro.api.dto.ApiDtos.PaymentResponse;
import com.smartparkpro.api.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    List<PaymentResponse> all() { return service.findAll(); }

    @GetMapping("/{id}")
    PaymentResponse one(@PathVariable UUID id) { return service.findById(id); }

    @PostMapping
    PaymentResponse create(@Valid @RequestBody PaymentRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    PaymentResponse update(@PathVariable UUID id, @Valid @RequestBody PaymentRequest request) { return service.update(id, request); }

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id) { service.delete(id); }
}
