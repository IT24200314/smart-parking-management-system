package com.smartparkpro.api.service;

import com.smartparkpro.api.dto.ApiDtos.QueueRequest;
import com.smartparkpro.api.dto.ApiDtos.QueueResponse;
import com.smartparkpro.api.entity.WaitingQueueEntry;
import com.smartparkpro.api.enums.ActivityType;
import com.smartparkpro.api.exception.ApiException;
import com.smartparkpro.api.repository.WaitingQueueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class QueueService {
    private final WaitingQueueRepository queue;
    private final VehicleService vehicleService;
    private final ParkingLotService lotService;
    private final ActivityService activityService;
    private final MapperService mapper;

    public QueueService(WaitingQueueRepository queue, VehicleService vehicleService, ParkingLotService lotService, ActivityService activityService, MapperService mapper) {
        this.queue = queue;
        this.vehicleService = vehicleService;
        this.lotService = lotService;
        this.activityService = activityService;
        this.mapper = mapper;
    }

    @Transactional
    public QueueResponse enqueue(QueueRequest request) {
        WaitingQueueEntry entry = new WaitingQueueEntry();
        entry.setVehicle(vehicleService.get(request.vehicleId()));
        entry.setParkingLot(lotService.get(request.parkingLotId()));
        entry.setRequestedType(request.requestedType());
        entry.setQueuePosition((int) queue.countByParkingLotId(request.parkingLotId()) + 1);
        WaitingQueueEntry saved = queue.save(entry);
        activityService.record(ActivityType.QUEUE_JOINED, saved.getVehicle(), null, "Vehicle joined waiting queue at position " + saved.getQueuePosition());
        return mapper.toQueue(saved);
    }

    public List<QueueResponse> byLot(UUID parkingLotId) {
        return queue.findByParkingLotIdOrderByQueuePositionAsc(parkingLotId).stream().map(mapper::toQueue).toList();
    }

    @Transactional
    public QueueResponse dequeue(UUID parkingLotId) {
        WaitingQueueEntry first = queue.findFirstByParkingLotIdOrderByQueuePositionAsc(parkingLotId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Waiting queue is empty"));
        queue.delete(first);
        return mapper.toQueue(first);
    }
}
