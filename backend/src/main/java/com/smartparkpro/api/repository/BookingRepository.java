package com.smartparkpro.api.repository;

import com.smartparkpro.api.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    long countByStartTimeBetween(Instant from, Instant to);
    Optional<Booking> findByTicketCode(String ticketCode);

    @Query("select b from Booking b where b.startTime >= :from order by b.startTime asc")
    List<Booking> findUpcoming(Instant from);
}
