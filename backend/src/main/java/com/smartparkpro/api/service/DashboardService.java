package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.DashboardResponse;
import com.smartparkpro.api.enums.PaymentStatus;
import com.smartparkpro.api.enums.SlotStatus;
import com.smartparkpro.api.repository.ParkingSlotRepository;
import com.smartparkpro.api.repository.PaymentRepository;
import com.smartparkpro.api.repository.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final VehicleRepository vehicles;
    private final ParkingSlotRepository slots;
    private final PaymentRepository payments;

    public DashboardService(VehicleRepository vehicles, ParkingSlotRepository slots, PaymentRepository payments) {
        this.vehicles = vehicles;
        this.slots = slots;
        this.payments = payments;
    }

    public DashboardResponse summary() {
        return new DashboardResponse(
                vehicles.count(),
                slots.countByStatus(SlotStatus.AVAILABLE),
                slots.countByStatus(SlotStatus.OCCUPIED),
                payments.sumAmountByStatus(PaymentStatus.PAID)
        );
    }
}
