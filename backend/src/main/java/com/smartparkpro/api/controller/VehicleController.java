package com.smartparkpro.api.controller;

import com.smartparkpro.api.dto.ApiDtos.VehicleRequest;
import com.smartparkpro.api.dto.ApiDtos.VehicleResponse;
import com.smartparkpro.api.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @GetMapping
    List<VehicleResponse> all() { return service.findAll(); }

    @GetMapping("/{id}")
    VehicleResponse one(@PathVariable UUID id) { return service.findById(id); }

    @PostMapping
    VehicleResponse create(@Valid @RequestBody VehicleRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    VehicleResponse update(@PathVariable UUID id, @Valid @RequestBody VehicleRequest request) { return service.update(id, request); }

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id) { service.delete(id); }
}
