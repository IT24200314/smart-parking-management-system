package com.smartparkpro.api.repository;

import com.smartparkpro.api.entity.Payment;
import com.smartparkpro.api.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    @Query("select sum(p.amount) from Payment p where p.status = :status")
    BigDecimal sumAmountByStatusNullable(PaymentStatus status);

    default BigDecimal sumAmountByStatus(PaymentStatus status) {
        return Optional.ofNullable(sumAmountByStatusNullable(status)).orElse(BigDecimal.ZERO);
    }
}
