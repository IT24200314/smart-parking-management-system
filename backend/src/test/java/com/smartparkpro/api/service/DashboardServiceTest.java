package com.smartparkpro.api.service;

import com.smartparkpro.api.enums.PaymentStatus;
import com.smartparkpro.api.enums.SlotStatus;
import com.smartparkpro.api.repository.ParkingSlotRepository;
import com.smartparkpro.api.repository.PaymentRepository;
import com.smartparkpro.api.repository.VehicleRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DashboardServiceTest {
    @Test
    void summaryAggregatesOperationalMetrics() {
        VehicleRepository vehicles = mock(VehicleRepository.class);
        ParkingSlotRepository slots = mock(ParkingSlotRepository.class);
        PaymentRepository payments = mock(PaymentRepository.class);
        when(vehicles.count()).thenReturn(12L);
        when(slots.countByStatus(SlotStatus.AVAILABLE)).thenReturn(8L);
        when(slots.countByStatus(SlotStatus.OCCUPIED)).thenReturn(4L);
        when(payments.sumAmountByStatus(PaymentStatus.PAID)).thenReturn(new BigDecimal("240.50"));

        var response = new DashboardService(vehicles, slots, payments).summary();

        assertThat(response.totalVehicles()).isEqualTo(12);
        assertThat(response.availableSlots()).isEqualTo(8);
        assertThat(response.occupiedSlots()).isEqualTo(4);
        assertThat(response.revenue()).isEqualByComparingTo("240.50");
    }
}
