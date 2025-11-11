package com.delivery.presenter.usecases;

import com.delivery.core.domain.model.DeliveryStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrackDeliveryUseCaseImplTest {

    private TrackDeliveryUseCaseImpl trackDeliveryUseCase;

    @BeforeEach
    void setUp() {
        trackDeliveryUseCase = new TrackDeliveryUseCaseImpl();
    }

    @Test
    void execute_shouldReturnDeliveryStatus() {
        String orderId = "ORDER123";
        DeliveryStatus result = trackDeliveryUseCase.execute(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertNotNull(result.getStatus());
        assertNotNull(result.getEstimatedDeliveryTime());
        assertNotNull(result.getLastUpdated());
        assertNotNull(result.getCurrentLocation());
    }
}