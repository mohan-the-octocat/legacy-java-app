package com.delivery.presenter.rest.controller;

import com.delivery.core.domain.model.DeliveryStatus;
import com.delivery.core.usecases.TrackDeliveryUseCase;
import com.delivery.presenter.rest.dto.DeliveryStatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryTrackingController.class)
class DeliveryTrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackDeliveryUseCase trackDeliveryUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void trackDelivery_shouldReturnDeliveryStatus() throws Exception {
        String orderId = "ORDER123";
        DeliveryStatus.Location location = new DeliveryStatus.Location(34.0522, -118.2437, "Los Angeles, CA");
        DeliveryStatus mockDeliveryStatus = new DeliveryStatus(
                orderId,
                "Shipped",
                OffsetDateTime.now().plusDays(2),
                OffsetDateTime.now(),
                location
        );

        when(trackDeliveryUseCase.execute(anyString())).thenReturn(mockDeliveryStatus);

        mockMvc.perform(get("/api/v1/delivery/track/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.deliveryStatus").value("Shipped"))
                .andExpect(jsonPath("$.estimatedDeliveryTime").exists())
                .andExpect(jsonPath("$.lastUpdated").exists())
                .andExpect(jsonPath("$.currentLocation.latitude").value(34.0522))
                .andExpect(jsonPath("$.currentLocation.longitude").value(-118.2437))
                .andExpect(jsonPath("$.currentLocation.address").value("Los Angeles, CA"));
    }
}