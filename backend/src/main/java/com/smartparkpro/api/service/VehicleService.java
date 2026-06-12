package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.VehicleRequest;
import com.smartparkpro.api.dto.ApiDtos.VehicleResponse;
import com.smartparkpro.api.entity.Vehicle;
import com.smartparkpro.api.exception.ApiException;
import com.smartparkpro.api.repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {
    private final VehicleRepository vehicles;
    private final UserService userService;
    private final MapperService mapper;

    public VehicleService(VehicleRepository vehicles, UserService userService, MapperService mapper) {
        this.vehicles = vehicles;
        this.userService = userService;
        this.mapper = mapper;
    }

    public List<VehicleResponse> findAll() {
        return vehicles.findAll().stream().map(mapper::toVehicle).toList();
    }

    public VehicleResponse findById(UUID id) {
        return mapper.toVehicle(get(id));
    }

    public VehicleResponse create(VehicleRequest request) {
        if (vehicles.existsByLicensePlate(request.licensePlate())) throw new ApiException(HttpStatus.CONFLICT, "License plate already exists");
        Vehicle vehicle = new Vehicle();
        apply(vehicle, request);
        return mapper.toVehicle(vehicles.save(vehicle));
    }

    public VehicleResponse update(UUID id, VehicleRequest request) {
        Vehicle vehicle = get(id);
        apply(vehicle, request);
        return mapper.toVehicle(vehicles.save(vehicle));
    }

    public void delete(UUID id) {
        vehicles.delete(get(id));
    }

    Vehicle get(UUID id) {
        return vehicles.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Vehicle not found"));
    }

    private void apply(Vehicle vehicle, VehicleRequest request) {
        vehicle.setOwner(userService.get(request.ownerId()));
        vehicle.setLicensePlate(request.licensePlate().toUpperCase());
        vehicle.setVehicleType(request.vehicleType());
        vehicle.setMake(request.make());
        vehicle.setModel(request.model());
        vehicle.setColor(request.color());
    }
}
