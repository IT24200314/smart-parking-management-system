package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.PaymentRequest;
import com.smartparkpro.api.dto.ApiDtos.PaymentResponse;
import com.smartparkpro.api.entity.Payment;
import com.smartparkpro.api.enums.ActivityType;
import com.smartparkpro.api.enums.PaymentStatus;
import com.smartparkpro.api.exception.ApiException;
import com.smartparkpro.api.repository.PaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository payments;
    private final BookingService bookingService;
    private final ActivityService activityService;
    private final NotificationService notificationService;
    private final MapperService mapper;

    public PaymentService(PaymentRepository payments, BookingService bookingService, ActivityService activityService, NotificationService notificationService, MapperService mapper) {
        this.payments = payments;
        this.bookingService = bookingService;
        this.activityService = activityService;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    public List<PaymentResponse> findAll() {
        return payments.findAll().stream().map(mapper::toPayment).toList();
    }

    public PaymentResponse findById(UUID id) {
        return mapper.toPayment(get(id));
    }

    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        Payment payment = new Payment();
        apply(payment, request);
        Payment saved = payments.save(payment);
        if (saved.getStatus() == PaymentStatus.PAID) {
            activityService.record(ActivityType.PAYMENT_CAPTURED, saved.getBooking().getVehicle(), saved.getBooking().getParkingSlot(), "Payment captured: " + saved.getAmount());
            notificationService.paymentReceipt(saved);
        }
        return mapper.toPayment(saved);
    }

    @Transactional
    public PaymentResponse update(UUID id, PaymentRequest request) {
        Payment payment = get(id);
        apply(payment, request);
        return mapper.toPayment(payments.save(payment));
    }

    @Transactional
    public void delete(UUID id) {
        payments.delete(get(id));
    }

    Payment get(UUID id) {
        return payments.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    private void apply(Payment payment, PaymentRequest request) {
        payment.setBooking(bookingService.get(request.bookingId()));
        payment.setAmount(request.amount());
        payment.setMethod(request.method());
        payment.setStatus(request.status());
        payment.setTransactionReference(request.transactionReference());
        payment.setPaidAt(request.status() == PaymentStatus.PAID ? Instant.now() : null);
    }
}
