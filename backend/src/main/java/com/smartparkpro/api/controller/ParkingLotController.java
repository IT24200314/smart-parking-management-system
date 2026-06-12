package com.smartparkpro.api.controller;

import com.smartparkpro.api.dto.ApiDtos.ParkingLotRequest;
import com.smartparkpro.api.dto.ApiDtos.ParkingLotResponse;
import com.smartparkpro.api.service.ParkingLotService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parking-lots")
public class ParkingLotController {
    private final ParkingLotService service;

    public ParkingLotController(ParkingLotService service) {
        this.service = service;
    }

    @GetMapping
    List<ParkingLotResponse> all() { return service.findAll(); }

    @GetMapping("/{id}")
    ParkingLotResponse one(@PathVariable UUID id) { return service.findById(id); }

    @PostMapping
    ParkingLotResponse create(@Valid @RequestBody ParkingLotRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    ParkingLotResponse update(@PathVariable UUID id, @Valid @RequestBody ParkingLotRequest request) { return service.update(id, request); }

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id) { service.delete(id); }
}
