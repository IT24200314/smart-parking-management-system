package com.smartparkpro.api.dto;

import com.smartparkpro.api.enums.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class ApiDtos {
    private ApiDtos() {}

    public record UserRequest(@NotBlank String fullName, @Email @NotBlank String email, String password, @NotNull Role role, boolean enabled) {}
    public record UserResponse(UUID id, String fullName, String email, Role role, boolean enabled) {}

    public record ParkingLotRequest(@NotBlank String name, @NotBlank String address, @Min(1) Integer capacity, @DecimalMin("0.00") BigDecimal hourlyRate, boolean active) {}
    public record ParkingLotResponse(UUID id, String name, String address, Integer capacity, BigDecimal hourlyRate, boolean active) {}

    public record ParkingSlotRequest(@NotNull UUID parkingLotId, @NotBlank String slotCode, @NotNull VehicleType supportedVehicleType, @NotNull SlotStatus status, boolean evCharger) {}
    public record ParkingSlotResponse(UUID id, UUID parkingLotId, String parkingLotName, String slotCode, VehicleType supportedVehicleType, SlotStatus status, boolean evCharger) {}

    public record VehicleRequest(@NotNull UUID ownerId, @NotBlank String licensePlate, @NotNull VehicleType vehicleType, String make, String model, String color) {}
    public record VehicleResponse(UUID id, UUID ownerId, String ownerName, String licensePlate, VehicleType vehicleType, String make, String model, String color) {}

    public record BookingRequest(@NotNull UUID userId, @NotNull UUID vehicleId, @NotNull UUID parkingSlotId, @NotNull Instant startTime, @Future Instant endTime, BookingStatus status, @DecimalMin("0.00") BigDecimal estimatedCost) {}
    public record BookingResponse(UUID id, UUID userId, String customerName, UUID vehicleId, String licensePlate, UUID parkingSlotId, String slotCode, Instant startTime, Instant endTime, BookingStatus status, BigDecimal estimatedCost, String ticketCode, String qrCodeBase64, Instant checkedInAt, Instant checkedOutAt) {}

    public record PaymentRequest(@NotNull UUID bookingId, @DecimalMin("0.00") BigDecimal amount, @NotNull PaymentMethod method, @NotNull PaymentStatus status, String transactionReference) {}
    public record PaymentResponse(UUID id, UUID bookingId, BigDecimal amount, PaymentMethod method, PaymentStatus status, String transactionReference, Instant paidAt) {}

    public record QueueRequest(@NotNull UUID vehicleId, @NotNull UUID parkingLotId, @NotNull VehicleType requestedType) {}
    public record QueueResponse(UUID id, UUID vehicleId, String licensePlate, UUID parkingLotId, String parkingLotName, VehicleType requestedType, Integer queuePosition) {}

    public record ActivityResponse(UUID id, ActivityType activityType, String description, String licensePlate, String slotCode, Instant createdAt) {}

    public record DashboardResponse(long totalVehicles, long availableSlots, long occupiedSlots, BigDecimal revenue) {}
    public record DemandPrediction(String label, long predictedVehicles, String confidence) {}
    public record SlotRecommendation(UUID slotId, String slotCode, String parkingLotName, VehicleType supportedVehicleType, boolean evCharger, String reason) {}
    public record AnalyticsResponse(List<DemandPrediction> demandPredictions, List<SlotRecommendation> recommendations, DashboardResponse dashboard) {}
    public record TicketScanRequest(@NotBlank String ticketCode) {}
    public record TicketScanResponse(UUID bookingId, String ticketCode, BookingStatus status, String slotCode, String licensePlate, Instant checkedInAt, Instant checkedOutAt, String message) {}
    public record AiDemandPrediction(double occupancyRate, long currentOccupiedSlots, long availableSlots, String peakHourPrediction, BigDecimal predictedDailyRevenue, List<DemandPrediction> demand) {}
}
