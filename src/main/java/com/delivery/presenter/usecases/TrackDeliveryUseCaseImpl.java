package com.delivery.presenter.usecases;

import com.delivery.core.domain.model.DeliveryStatus;
import com.delivery.core.usecases.TrackDeliveryUseCase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class TrackDeliveryUseCaseImpl implements TrackDeliveryUseCase {

    @Override
    public DeliveryStatus execute(String orderId) {
        // In a real scenario, this would involve:
        // 1. Calling OMS Integration Service to get basic order details.
        // 2. Calling 3PL Integration Service to get real-time tracking data.
        // 3. Aggregating the data into a DeliveryStatus object.

        // For now, returning a dummy response
        return new DeliveryStatus(
                orderId,
                "Processing",
                OffsetDateTime.now().plusDays(3),
                OffsetDateTime.now(),
                new DeliveryStatus.Location(34.0522, -118.2437, "Los Angeles, CA")
        );
    }
}