package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.AnalyticsResponse;
import com.smartparkpro.api.dto.ApiDtos.AiDemandPrediction;
import com.smartparkpro.api.dto.ApiDtos.DemandPrediction;
import com.smartparkpro.api.dto.ApiDtos.SlotRecommendation;
import com.smartparkpro.api.enums.PaymentStatus;
import com.smartparkpro.api.enums.SlotStatus;
import com.smartparkpro.api.enums.VehicleType;
import com.smartparkpro.api.repository.BookingRepository;
import com.smartparkpro.api.repository.ParkingSlotRepository;
import com.smartparkpro.api.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AnalyticsService {
    private final BookingRepository bookings;
    private final ParkingSlotRepository slots;
    private final PaymentRepository payments;
    private final DashboardService dashboardService;

    public AnalyticsService(BookingRepository bookings, ParkingSlotRepository slots, PaymentRepository payments, DashboardService dashboardService) {
        this.bookings = bookings;
        this.slots = slots;
        this.payments = payments;
        this.dashboardService = dashboardService;
    }

    public List<DemandPrediction> predictDemand() {
        Instant now = Instant.now();
        long today = bookings.countByStartTimeBetween(now.minus(1, ChronoUnit.DAYS), now);
        long week = bookings.countByStartTimeBetween(now.minus(7, ChronoUnit.DAYS), now);
        long baseline = Math.max(today, Math.round(week / 7.0));
        return List.of(
                new DemandPrediction("Next hour", Math.max(1, Math.round(baseline * 0.18)), "medium"),
                new DemandPrediction("Today", Math.max(1, Math.round(baseline * 1.12)), "medium"),
                new DemandPrediction("This weekend", Math.max(1, Math.round((week / 7.0) * 2.4)), "low")
        );
    }

    public List<SlotRecommendation> recommendSlots(VehicleType vehicleType) {
        return slots.findTop5ByStatusAndSupportedVehicleTypeOrderBySlotCodeAsc(SlotStatus.AVAILABLE, vehicleType)
                .stream()
                .map(slot -> new SlotRecommendation(
                        slot.getId(),
                        slot.getSlotCode(),
                        slot.getParkingLot().getName(),
                        slot.getSupportedVehicleType(),
                        slot.isEvCharger(),
                        slot.isEvCharger() ? "Available slot with EV charging" : "Nearest available compatible slot"
                ))
                .toList();
    }

    public AnalyticsResponse analytics(VehicleType vehicleType) {
        return new AnalyticsResponse(predictDemand(), recommendSlots(vehicleType), dashboardService.summary());
    }

    public AiDemandPrediction predictDemandAdvanced() {
        var dashboard = dashboardService.summary();
        long totalTrackedSlots = dashboard.availableSlots() + dashboard.occupiedSlots();
        double occupancyRate = totalTrackedSlots == 0 ? 0 : (dashboard.occupiedSlots() * 100.0) / totalTrackedSlots;
        long last24h = bookings.countByStartTimeBetween(Instant.now().minus(24, ChronoUnit.HOURS), Instant.now());
        long last7d = bookings.countByStartTimeBetween(Instant.now().minus(7, ChronoUnit.DAYS), Instant.now());
        long hourlyBaseline = Math.max(1, Math.round(last24h / 24.0));
        long dailyBaseline = Math.max(1, Math.round(last7d / 7.0));
        String peak = hourlyBaseline >= 3 ? "Next 2 hours" : dailyBaseline >= 10 ? "Evening commute" : "Stable demand window";
        BigDecimal revenue = payments.sumAmountByStatus(PaymentStatus.PAID);
        BigDecimal predictedRevenue = revenue
                .add(BigDecimal.valueOf(dailyBaseline).multiply(BigDecimal.valueOf(6.50)))
                .setScale(2, RoundingMode.HALF_UP);
        return new AiDemandPrediction(
                Math.round(occupancyRate * 100.0) / 100.0,
                dashboard.occupiedSlots(),
                dashboard.availableSlots(),
                peak,
                predictedRevenue,
                predictDemand()
        );
    }
}
