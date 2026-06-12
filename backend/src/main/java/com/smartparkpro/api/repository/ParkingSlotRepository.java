package com.smartparkpro.api.repository;

import com.smartparkpro.api.entity.ParkingSlot;
import com.smartparkpro.api.enums.SlotStatus;
import com.smartparkpro.api.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, UUID> {
    long countByStatus(SlotStatus status);
    List<ParkingSlot> findTop5ByStatusAndSupportedVehicleTypeOrderBySlotCodeAsc(SlotStatus status, VehicleType vehicleType);
    Optional<ParkingSlot> findFirstByStatusAndParkingLotIdAndSupportedVehicleTypeOrderBySlotCodeAsc(SlotStatus status, UUID parkingLotId, VehicleType vehicleType);
}
