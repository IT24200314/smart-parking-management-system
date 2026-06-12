package com.smartparkpro.api.repository;

import com.smartparkpro.api.entity.ParkingActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParkingActivityRepository extends JpaRepository<ParkingActivity, UUID> {
    List<ParkingActivity> findTop20ByOrderByCreatedAtDesc();
}
