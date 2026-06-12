package com.smartparkpro.api.controller;

import com.smartparkpro.api.entity.User;
import com.smartparkpro.api.enums.Role;
import com.smartparkpro.api.repository.UserRepository;
import com.smartparkpro.api.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DashboardControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository users;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtService jwtService;

    @Test
    void dashboardRequiresAuthorizedStaffRole() throws Exception {
        User user = new User();
        user.setFullName("Staff");
        user.setEmail("staff-test@example.com");
        user.setPasswordHash(encoder.encode("password123"));
        user.setRole(Role.STAFF);
        user = users.save(user);

        mockMvc.perform(get("/api/dashboard").header("Authorization", "Bearer " + jwtService.generate(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVehicles").exists());
    }
}
