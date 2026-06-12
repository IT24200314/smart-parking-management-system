package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.ParkingSlotRequest;
import com.smartparkpro.api.dto.ApiDtos.ParkingSlotResponse;
import com.smartparkpro.api.entity.ParkingSlot;
import com.smartparkpro.api.exception.ApiException;
import com.smartparkpro.api.repository.ParkingSlotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParkingSlotService {
    private final ParkingSlotRepository slots;
    private final ParkingLotService lotService;
    private final MapperService mapper;

    public ParkingSlotService(ParkingSlotRepository slots, ParkingLotService lotService, MapperService mapper) {
        this.slots = slots;
        this.lotService = lotService;
        this.mapper = mapper;
    }

    public List<ParkingSlotResponse> findAll() {
        return slots.findAll().stream().map(mapper::toSlot).toList();
    }

    public ParkingSlotResponse findById(UUID id) {
        return mapper.toSlot(get(id));
    }

    public ParkingSlotResponse create(ParkingSlotRequest request) {
        ParkingSlot slot = new ParkingSlot();
        apply(slot, request);
        return mapper.toSlot(slots.save(slot));
    }

    public ParkingSlotResponse update(UUID id, ParkingSlotRequest request) {
        ParkingSlot slot = get(id);
        apply(slot, request);
        return mapper.toSlot(slots.save(slot));
    }

    public void delete(UUID id) {
        slots.delete(get(id));
    }

    ParkingSlot get(UUID id) {
        return slots.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Parking slot not found"));
    }

    private void apply(ParkingSlot slot, ParkingSlotRequest request) {
        slot.setParkingLot(lotService.get(request.parkingLotId()));
        slot.setSlotCode(request.slotCode());
        slot.setSupportedVehicleType(request.supportedVehicleType());
        slot.setStatus(request.status());
        slot.setEvCharger(request.evCharger());
    }
}
