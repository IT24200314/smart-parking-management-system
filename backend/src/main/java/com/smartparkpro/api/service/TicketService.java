package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.TicketScanResponse;
import com.smartparkpro.api.entity.Booking;
import com.smartparkpro.api.enums.ActivityType;
import com.smartparkpro.api.enums.BookingStatus;
import com.smartparkpro.api.enums.SlotStatus;
import com.smartparkpro.api.exception.ApiException;
import com.smartparkpro.api.repository.BookingRepository;
import com.smartparkpro.api.repository.ParkingSlotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TicketService {
    private final BookingRepository bookings;
    private final ParkingSlotRepository slots;
    private final ActivityService activityService;

    public TicketService(BookingRepository bookings, ParkingSlotRepository slots, ActivityService activityService) {
        this.bookings = bookings;
        this.slots = slots;
        this.activityService = activityService;
    }

    @Transactional
    public TicketScanResponse scanEntry(String ticketCode) {
        Booking booking = booking(ticketCode);
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new ApiException(HttpStatus.CONFLICT, "Ticket is not valid for entry");
        }
        booking.setStatus(BookingStatus.CHECKED_IN);
        booking.setCheckedInAt(Instant.now());
        booking.getParkingSlot().setStatus(SlotStatus.OCCUPIED);
        slots.save(booking.getParkingSlot());
        Booking saved = bookings.save(booking);
        activityService.record(ActivityType.CHECK_IN, saved.getVehicle(), saved.getParkingSlot(), "QR entry scan accepted for " + saved.getTicketCode());
        return response(saved, "Entry scan accepted");
    }

    @Transactional
    public TicketScanResponse scanExit(String ticketCode) {
        Booking booking = booking(ticketCode);
        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new ApiException(HttpStatus.CONFLICT, "Vehicle must be checked in before exit");
        }
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCheckedOutAt(Instant.now());
        booking.getParkingSlot().setStatus(SlotStatus.AVAILABLE);
        slots.save(booking.getParkingSlot());
        Booking saved = bookings.save(booking);
        activityService.record(ActivityType.CHECK_OUT, saved.getVehicle(), saved.getParkingSlot(), "QR exit scan accepted for " + saved.getTicketCode());
        return response(saved, "Exit scan accepted");
    }

    private Booking booking(String ticketCode) {
        return bookings.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    private TicketScanResponse response(Booking booking, String message) {
        return new TicketScanResponse(
                booking.getId(),
                booking.getTicketCode(),
                booking.getStatus(),
                booking.getParkingSlot().getSlotCode(),
                booking.getVehicle().getLicensePlate(),
                booking.getCheckedInAt(),
                booking.getCheckedOutAt(),
                message
        );
    }
}
