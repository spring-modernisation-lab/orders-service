package com.springmodernisationlab.orders.web;

import com.springmodernisationlab.orders.persistence.CustomerOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerOrderRepository repository;

    @BeforeEach
    void clearOrders() {
        repository.deleteAll();
    }

    @Test
    void operatorCanCreateOrderAndViewerCanReadItBack() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .with(httpBasic("operator", "operator-password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reference\":\"ORD-1001\",\"amount\":12.5,\"currency\":\"gbp\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reference").value("ORD-1001"))
                .andExpect(jsonPath("$.status").value("PLACED"));

        mockMvc.perform(get("/api/orders/ORD-1001")
                        .with(httpBasic("viewer", "viewer-password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(12.50))
                .andExpect(jsonPath("$.currency").value("GBP"));
    }

    @Test
    void viewerCannotCreateOrders() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .with(httpBasic("viewer", "viewer-password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reference\":\"ORD-1002\",\"amount\":5,\"currency\":\"GBP\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void anonymousRequestsAreRejected() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidOrdersAreRejected() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .with(httpBasic("operator", "operator-password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reference\":\"\",\"amount\":0,\"currency\":\"GB\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void legacyClientsCanListOrdersUsingTrailingSlash() throws Exception {
        // The warehouse integration has called "/api/orders/" since v1 of the API.
        mockMvc.perform(get("/api/orders/")
                        .with(httpBasic("viewer", "viewer-password")))
                .andExpect(status().isOk());
    }
}
