package com.smartparkpro.api.repository;

import com.smartparkpro.api.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, UUID> {
}
