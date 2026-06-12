package com.smartparkpro.api.service;

import com.smartparkpro.api.entity.ParkingLot;
import com.smartparkpro.api.entity.ParkingSlot;
import com.smartparkpro.api.enums.SlotStatus;
import com.smartparkpro.api.enums.VehicleType;
import com.smartparkpro.api.repository.BookingRepository;
import com.smartparkpro.api.repository.ParkingSlotRepository;
import com.smartparkpro.api.repository.PaymentRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnalyticsServiceTest {
    @Test
    void recommendsCompatibleAvailableSlots() {
        BookingRepository bookings = mock(BookingRepository.class);
        ParkingSlotRepository slots = mock(ParkingSlotRepository.class);
        DashboardService dashboard = mock(DashboardService.class);

        ParkingLot lot = new ParkingLot();
        lot.setName("Central");
        lot.setHourlyRate(BigDecimal.TEN);
        lot.setCapacity(20);
        lot.setAddress("Main");

        ParkingSlot slot = new ParkingSlot();
        slot.setId(UUID.randomUUID());
        slot.setParkingLot(lot);
        slot.setSlotCode("A-02");
        slot.setSupportedVehicleType(VehicleType.EV);
        slot.setStatus(SlotStatus.AVAILABLE);
        slot.setEvCharger(true);
        when(slots.findTop5ByStatusAndSupportedVehicleTypeOrderBySlotCodeAsc(SlotStatus.AVAILABLE, VehicleType.EV)).thenReturn(List.of(slot));

        var recommendations = new AnalyticsService(bookings, slots, mock(PaymentRepository.class), dashboard).recommendSlots(VehicleType.EV);

        assertThat(recommendations).hasSize(1);
        assertThat(recommendations.get(0).reason()).contains("EV");
    }

    @Test
    void predictionUsesRecentBookingCounts() {
        BookingRepository bookings = mock(BookingRepository.class);
        when(bookings.countByStartTimeBetween(any(Instant.class), any(Instant.class))).thenReturn(14L);

        var predictions = new AnalyticsService(bookings, mock(ParkingSlotRepository.class), mock(PaymentRepository.class), mock(DashboardService.class)).predictDemand();

        assertThat(predictions).hasSize(3);
        assertThat(predictions.get(1).predictedVehicles()).isGreaterThan(0);
    }
}
