package com.smartparkpro.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CrudFlowIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void adminCanOperateCoreParkingWorkflow() throws Exception {
        String token = registerAdmin();

        String customerId = id(postJson("/api/users", token, """
                {"fullName":"Flow Customer","email":"flow-customer@example.com","password":"password123","role":"CUSTOMER","enabled":true}
                """));
        String lotId = id(postJson("/api/parking-lots", token, """
                {"name":"Flow Garage","address":"1 Flow Street","capacity":10,"hourlyRate":6.75,"active":true}
                """));
        String slotId = id(postJson("/api/parking-slots", token, """
                {"parkingLotId":"%s","slotCode":"F-01","supportedVehicleType":"CAR","status":"AVAILABLE","evCharger":false}
                """.formatted(lotId)));
        String vehicleId = id(postJson("/api/vehicles", token, """
                {"ownerId":"%s","licensePlate":"FLOW-101","vehicleType":"CAR","make":"Honda","model":"Civic","color":"Blue"}
                """.formatted(customerId)));

        Instant start = Instant.now().plus(1, ChronoUnit.HOURS);
        Instant end = start.plus(2, ChronoUnit.HOURS);
        String bookingId = id(postJson("/api/bookings", token, """
                {"userId":"%s","vehicleId":"%s","parkingSlotId":"%s","startTime":"%s","endTime":"%s","status":"CONFIRMED","estimatedCost":13.50}
                """.formatted(customerId, vehicleId, slotId, start, end)));
        JsonNode booking = getJson("/api/bookings/" + bookingId, token);
        String ticketCode = booking.get("ticketCode").asText();

        String paymentId = id(postJson("/api/payments", token, """
                {"bookingId":"%s","amount":13.50,"method":"CARD","status":"PAID","transactionReference":"FLOW-TXN"}
                """.formatted(bookingId)));

        mockMvc.perform(get("/api/dashboard").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVehicles").value(1))
                .andExpect(jsonPath("$.revenue").value(13.5));

        mockMvc.perform(get("/api/analytics?vehicleType=CAR").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.demandPredictions").isArray());

        mockMvc.perform(get("/api/ai/predict-demand").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.occupancyRate").exists());

        mockMvc.perform(post("/api/tickets/scan-entry").header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"ticketCode":"%s"}
                                """.formatted(ticketCode)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CHECKED_IN"));

        mockMvc.perform(post("/api/tickets/scan-exit").header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"ticketCode":"%s"}
                                """.formatted(ticketCode)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        postJson("/api/waiting-queue", token, """
                {"vehicleId":"%s","parkingLotId":"%s","requestedType":"CAR"}
                """.formatted(vehicleId, lotId));

        mockMvc.perform(get("/api/waiting-queue/" + lotId).header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].queuePosition").value(1));

        mockMvc.perform(delete("/api/waiting-queue/" + lotId + "/next").header("Authorization", bearer(token)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/recent-activities").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(delete("/api/payments/" + paymentId).header("Authorization", bearer(token))).andExpect(status().isOk());
        mockMvc.perform(delete("/api/bookings/" + bookingId).header("Authorization", bearer(token))).andExpect(status().isOk());
        mockMvc.perform(delete("/api/vehicles/" + vehicleId).header("Authorization", bearer(token))).andExpect(status().isOk());
        mockMvc.perform(delete("/api/parking-slots/" + slotId).header("Authorization", bearer(token))).andExpect(status().isOk());
        mockMvc.perform(delete("/api/parking-lots/" + lotId).header("Authorization", bearer(token))).andExpect(status().isOk());
    }

    private String registerAdmin() throws Exception {
        JsonNode node = postJson("/api/auth/register", null, """
                {"fullName":"Flow Admin","email":"flow-admin@example.com","password":"password123","role":"ADMIN"}
                """);
        return node.get("token").asText();
    }

    private JsonNode postJson(String path, String token, String body) throws Exception {
        var request = post(path).contentType(MediaType.APPLICATION_JSON).content(body);
        if (token != null) request.header("Authorization", bearer(token));
        String response = mockMvc.perform(request).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response);
    }

    private JsonNode getJson(String path, String token) throws Exception {
        String response = mockMvc.perform(get(path).header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response);
    }

    private String id(JsonNode node) {
        return node.get("id").asText();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
