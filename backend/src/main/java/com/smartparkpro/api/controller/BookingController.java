package com.smartparkpro.api.controller;

import com.smartparkpro.api.dto.ApiDtos.BookingRequest;
import com.smartparkpro.api.dto.ApiDtos.BookingResponse;
import com.smartparkpro.api.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @GetMapping
    List<BookingResponse> all() { return service.findAll(); }

    @GetMapping("/{id}")
    BookingResponse one(@PathVariable UUID id) { return service.findById(id); }

    @PostMapping
    BookingResponse create(@Valid @RequestBody BookingRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    BookingResponse update(@PathVariable UUID id, @Valid @RequestBody BookingRequest request) { return service.update(id, request); }

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id) { service.delete(id); }
}
