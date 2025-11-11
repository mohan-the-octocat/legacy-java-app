package com.delivery.core.usecases;

import com.delivery.core.domain.model.DeliveryStatus;

public interface TrackDeliveryUseCase {
    DeliveryStatus execute(String orderId);
}