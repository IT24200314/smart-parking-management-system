package com.smartparkpro.api.repository;

import com.smartparkpro.api.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    boolean existsByLicensePlate(String licensePlate);
}
