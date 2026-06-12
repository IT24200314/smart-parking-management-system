package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.ActivityResponse;
import com.smartparkpro.api.entity.ParkingActivity;
import com.smartparkpro.api.entity.ParkingSlot;
import com.smartparkpro.api.entity.Vehicle;
import com.smartparkpro.api.enums.ActivityType;
import com.smartparkpro.api.repository.ParkingActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    private final ParkingActivityRepository activities;
    private final MapperService mapper;

    public ActivityService(ParkingActivityRepository activities, MapperService mapper) {
        this.activities = activities;
        this.mapper = mapper;
    }

    public void record(ActivityType type, Vehicle vehicle, ParkingSlot slot, String description) {
        ParkingActivity activity = new ParkingActivity();
        activity.setActivityType(type);
        activity.setVehicle(vehicle);
        activity.setParkingSlot(slot);
        activity.setDescription(description);
        activities.save(activity);
    }

    public List<ActivityResponse> recentStack() {
        return activities.findTop20ByOrderByCreatedAtDesc().stream().map(mapper::toActivity).toList();
    }
}
