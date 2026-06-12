package com.smartparkpro.api.controller;

import com.smartparkpro.api.dto.ApiDtos.ParkingSlotRequest;
import com.smartparkpro.api.dto.ApiDtos.ParkingSlotResponse;
import com.smartparkpro.api.service.ParkingSlotService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parking-slots")
public class ParkingSlotController {
    private final ParkingSlotService service;

    public ParkingSlotController(ParkingSlotService service) {
        this.service = service;
    }

    @GetMapping
    List<ParkingSlotResponse> all() { return service.findAll(); }

    @GetMapping("/{id}")
    ParkingSlotResponse one(@PathVariable UUID id) { return service.findById(id); }

    @PostMapping
    ParkingSlotResponse create(@Valid @RequestBody ParkingSlotRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    ParkingSlotResponse update(@PathVariable UUID id, @Valid @RequestBody ParkingSlotRequest request) { return service.update(id, request); }

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id) { service.delete(id); }
}
