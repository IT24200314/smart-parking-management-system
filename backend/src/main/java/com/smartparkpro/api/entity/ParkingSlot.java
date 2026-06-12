package com.smartparkpro.api.entity;

import com.smartparkpro.api.enums.SlotStatus;
import com.smartparkpro.api.enums.VehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "parking_slots", uniqueConstraints = {
        @UniqueConstraint(name = "uk_slot_lot_code", columnNames = {"parking_lot_id", "slot_code"})
})
public class ParkingSlot extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot parkingLot;

    @Column(name = "slot_code", nullable = false)
    private String slotCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType supportedVehicleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status = SlotStatus.AVAILABLE;

    @Column(nullable = false)
    private boolean evCharger = false;
}
