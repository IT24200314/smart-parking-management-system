package com.smartparkpro.api.repository;

import com.smartparkpro.api.entity.ParkingLot;
import com.smartparkpro.api.entity.ParkingSlot;
import com.smartparkpro.api.enums.SlotStatus;
import com.smartparkpro.api.enums.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ParkingRepositoryTest {
    @Autowired
    ParkingLotRepository lots;

    @Autowired
    ParkingSlotRepository slots;

    @Test
    void countsAvailableSlotsAndFindsRecommendations() {
        ParkingLot lot = new ParkingLot();
        lot.setName("Test Garage");
        lot.setAddress("Test Street");
        lot.setCapacity(2);
        lot.setHourlyRate(new BigDecimal("4.50"));
        lot = lots.save(lot);

        ParkingSlot slot = new ParkingSlot();
        slot.setParkingLot(lot);
        slot.setSlotCode("T-01");
        slot.setSupportedVehicleType(VehicleType.CAR);
        slot.setStatus(SlotStatus.AVAILABLE);
        slots.save(slot);

        assertThat(slots.countByStatus(SlotStatus.AVAILABLE)).isEqualTo(1);
        assertThat(slots.findTop5ByStatusAndSupportedVehicleTypeOrderBySlotCodeAsc(SlotStatus.AVAILABLE, VehicleType.CAR)).hasSize(1);
    }
}
