package com.smartparkpro.api.service;

import com.smartparkpro.api.entity.Booking;
import com.smartparkpro.api.entity.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final JavaMailSender mailSender;
    private final boolean enabled;
    private final String from;

    public NotificationService(JavaMailSender mailSender,
                               @Value("${app.notifications.enabled}") boolean enabled,
                               @Value("${app.notifications.from}") String from) {
        this.mailSender = mailSender;
        this.enabled = enabled;
        this.from = from;
    }

    public void bookingConfirmation(Booking booking) {
        send(booking.getUser().getEmail(), "SmartParkPro booking confirmation",
                "Your booking is confirmed. Ticket: %s, Slot: %s".formatted(booking.getTicketCode(), booking.getParkingSlot().getSlotCode()));
    }

    public void slotAssignment(Booking booking) {
        send(booking.getUser().getEmail(), "SmartParkPro slot assignment",
                "Your assigned slot is %s at %s.".formatted(booking.getParkingSlot().getSlotCode(), booking.getParkingSlot().getParkingLot().getName()));
    }

    public void paymentReceipt(Payment payment) {
        send(payment.getBooking().getUser().getEmail(), "SmartParkPro payment receipt",
                "Payment received: %s. Reference: %s".formatted(payment.getAmount(), payment.getTransactionReference()));
    }

    private void send(String to, String subject, String text) {
        if (!enabled) return;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (MailException ignored) {
            // Notifications should not block parking operations.
        }
    }
}
