package com.smartparkpro.api.controller;

import com.smartparkpro.api.dto.ApiDtos.*;
import com.smartparkpro.api.enums.VehicleType;
import com.smartparkpro.api.service.ActivityService;
import com.smartparkpro.api.service.AnalyticsService;
import com.smartparkpro.api.service.DashboardService;
import com.smartparkpro.api.service.QueueService;
import com.smartparkpro.api.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class OperationsController {
    private final DashboardService dashboardService;
    private final AnalyticsService analyticsService;
    private final QueueService queueService;
    private final ActivityService activityService;
    private final TicketService ticketService;

    public OperationsController(DashboardService dashboardService, AnalyticsService analyticsService, QueueService queueService, ActivityService activityService, TicketService ticketService) {
        this.dashboardService = dashboardService;
        this.analyticsService = analyticsService;
        this.queueService = queueService;
        this.activityService = activityService;
        this.ticketService = ticketService;
    }

    @GetMapping("/api/dashboard")
    DashboardResponse dashboard() { return dashboardService.summary(); }

    @GetMapping("/api/analytics")
    AnalyticsResponse analytics(@RequestParam(defaultValue = "CAR") VehicleType vehicleType) { return analyticsService.analytics(vehicleType); }

    @GetMapping("/api/ai/predict-demand")
    AiDemandPrediction predictDemand() { return analyticsService.predictDemandAdvanced(); }

    @PostMapping("/api/tickets/scan-entry")
    TicketScanResponse scanEntry(@Valid @RequestBody TicketScanRequest request) { return ticketService.scanEntry(request.ticketCode()); }

    @PostMapping("/api/tickets/scan-exit")
    TicketScanResponse scanExit(@Valid @RequestBody TicketScanRequest request) { return ticketService.scanExit(request.ticketCode()); }

    @PostMapping("/api/waiting-queue")
    QueueResponse enqueue(@Valid @RequestBody QueueRequest request) { return queueService.enqueue(request); }

    @GetMapping("/api/waiting-queue/{parkingLotId}")
    List<QueueResponse> queue(@PathVariable UUID parkingLotId) { return queueService.byLot(parkingLotId); }

    @DeleteMapping("/api/waiting-queue/{parkingLotId}/next")
    QueueResponse dequeue(@PathVariable UUID parkingLotId) { return queueService.dequeue(parkingLotId); }

    @GetMapping("/api/recent-activities")
    List<ActivityResponse> recentActivities() { return activityService.recentStack(); }
}
