package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.*;
import com.smartparkpro.api.entity.*;
import org.springframework.stereotype.Component;

@Component
public class MapperService {
    UserResponse toUser(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole(), user.isEnabled());
    }

    ParkingLotResponse toLot(ParkingLot lot) {
        return new ParkingLotResponse(lot.getId(), lot.getName(), lot.getAddress(), lot.getCapacity(), lot.getHourlyRate(), lot.isActive());
    }

    ParkingSlotResponse toSlot(ParkingSlot slot) {
        return new ParkingSlotResponse(slot.getId(), slot.getParkingLot().getId(), slot.getParkingLot().getName(), slot.getSlotCode(), slot.getSupportedVehicleType(), slot.getStatus(), slot.isEvCharger());
    }

    VehicleResponse toVehicle(Vehicle vehicle) {
        return new VehicleResponse(vehicle.getId(), vehicle.getOwner().getId(), vehicle.getOwner().getFullName(), vehicle.getLicensePlate(), vehicle.getVehicleType(), vehicle.getMake(), vehicle.getModel(), vehicle.getColor());
    }

    BookingResponse toBooking(Booking booking) {
        return new BookingResponse(booking.getId(), booking.getUser().getId(), booking.getUser().getFullName(), booking.getVehicle().getId(), booking.getVehicle().getLicensePlate(), booking.getParkingSlot().getId(), booking.getParkingSlot().getSlotCode(), booking.getStartTime(), booking.getEndTime(), booking.getStatus(), booking.getEstimatedCost(), booking.getTicketCode(), booking.getQrCodeBase64(), booking.getCheckedInAt(), booking.getCheckedOutAt());
    }

    PaymentResponse toPayment(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getBooking().getId(), payment.getAmount(), payment.getMethod(), payment.getStatus(), payment.getTransactionReference(), payment.getPaidAt());
    }

    QueueResponse toQueue(WaitingQueueEntry entry) {
        return new QueueResponse(entry.getId(), entry.getVehicle().getId(), entry.getVehicle().getLicensePlate(), entry.getParkingLot().getId(), entry.getParkingLot().getName(), entry.getRequestedType(), entry.getQueuePosition());
    }

    ActivityResponse toActivity(ParkingActivity activity) {
        String plate = activity.getVehicle() == null ? null : activity.getVehicle().getLicensePlate();
        String slot = activity.getParkingSlot() == null ? null : activity.getParkingSlot().getSlotCode();
        return new ActivityResponse(activity.getId(), activity.getActivityType(), activity.getDescription(), plate, slot, activity.getCreatedAt());
    }
}
