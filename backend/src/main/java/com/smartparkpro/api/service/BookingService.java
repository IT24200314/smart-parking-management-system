package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.BookingRequest;
import com.smartparkpro.api.dto.ApiDtos.BookingResponse;
import com.smartparkpro.api.entity.Booking;
import com.smartparkpro.api.enums.ActivityType;
import com.smartparkpro.api.enums.BookingStatus;
import com.smartparkpro.api.exception.ApiException;
import com.smartparkpro.api.repository.BookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookings;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final ParkingSlotService slotService;
    private final ActivityService activityService;
    private final QrCodeService qrCodeService;
    private final NotificationService notificationService;
    private final MapperService mapper;

    public BookingService(BookingRepository bookings, UserService userService, VehicleService vehicleService, ParkingSlotService slotService, ActivityService activityService, QrCodeService qrCodeService, NotificationService notificationService, MapperService mapper) {
        this.bookings = bookings;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.slotService = slotService;
        this.activityService = activityService;
        this.qrCodeService = qrCodeService;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    public List<BookingResponse> findAll() {
        return bookings.findAll().stream().map(mapper::toBooking).toList();
    }

    public BookingResponse findById(UUID id) {
        return mapper.toBooking(get(id));
    }

    @Transactional
    public BookingResponse create(BookingRequest request) {
        Booking booking = new Booking();
        apply(booking, request);
        String ticketCode = "SPP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
        String payload = "SMARTPARKPRO:%s:%s:%s".formatted(ticketCode, booking.getVehicle().getLicensePlate(), booking.getParkingSlot().getSlotCode());
        booking.setTicketCode(ticketCode);
        booking.setQrPayload(payload);
        booking.setQrCodeBase64(qrCodeService.generateBase64Png(payload));
        Booking saved = bookings.save(booking);
        activityService.record(ActivityType.BOOKING_CREATED, saved.getVehicle(), saved.getParkingSlot(), "Booking created for " + saved.getVehicle().getLicensePlate());
        notificationService.bookingConfirmation(saved);
        notificationService.slotAssignment(saved);
        return mapper.toBooking(saved);
    }

    @Transactional
    public BookingResponse update(UUID id, BookingRequest request) {
        Booking booking = get(id);
        apply(booking, request);
        return mapper.toBooking(bookings.save(booking));
    }

    @Transactional
    public void delete(UUID id) {
        bookings.delete(get(id));
    }

    Booking get(UUID id) {
        return bookings.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    private void apply(Booking booking, BookingRequest request) {
        booking.setUser(userService.get(request.userId()));
        booking.setVehicle(vehicleService.get(request.vehicleId()));
        booking.setParkingSlot(slotService.get(request.parkingSlotId()));
        booking.setStartTime(request.startTime());
        booking.setEndTime(request.endTime());
        booking.setStatus(request.status() == null ? BookingStatus.PENDING : request.status());
        booking.setEstimatedCost(request.estimatedCost());
    }
}
