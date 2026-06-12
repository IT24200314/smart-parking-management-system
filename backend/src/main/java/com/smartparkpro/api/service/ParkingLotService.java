package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.ParkingLotRequest;
import com.smartparkpro.api.dto.ApiDtos.ParkingLotResponse;
import com.smartparkpro.api.entity.ParkingLot;
import com.smartparkpro.api.exception.ApiException;
import com.smartparkpro.api.repository.ParkingLotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParkingLotService {
    private final ParkingLotRepository lots;
    private final MapperService mapper;

    public ParkingLotService(ParkingLotRepository lots, MapperService mapper) {
        this.lots = lots;
        this.mapper = mapper;
    }

    public List<ParkingLotResponse> findAll() {
        return lots.findAll().stream().map(mapper::toLot).toList();
    }

    public ParkingLotResponse findById(UUID id) {
        return mapper.toLot(get(id));
    }

    public ParkingLotResponse create(ParkingLotRequest request) {
        ParkingLot lot = new ParkingLot();
        apply(lot, request);
        return mapper.toLot(lots.save(lot));
    }

    public ParkingLotResponse update(UUID id, ParkingLotRequest request) {
        ParkingLot lot = get(id);
        apply(lot, request);
        return mapper.toLot(lots.save(lot));
    }

    public void delete(UUID id) {
        lots.delete(get(id));
    }

    ParkingLot get(UUID id) {
        return lots.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Parking lot not found"));
    }

    private void apply(ParkingLot lot, ParkingLotRequest request) {
        lot.setName(request.name());
        lot.setAddress(request.address());
        lot.setCapacity(request.capacity());
        lot.setHourlyRate(request.hourlyRate());
        lot.setActive(request.active());
    }
}
