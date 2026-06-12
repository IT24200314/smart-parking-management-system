package com.smartparkpro.api.repository;

import com.smartparkpro.api.entity.WaitingQueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WaitingQueueRepository extends JpaRepository<WaitingQueueEntry, UUID> {
    List<WaitingQueueEntry> findByParkingLotIdOrderByQueuePositionAsc(UUID parkingLotId);
    Optional<WaitingQueueEntry> findFirstByParkingLotIdOrderByQueuePositionAsc(UUID parkingLotId);
    long countByParkingLotId(UUID parkingLotId);
}
