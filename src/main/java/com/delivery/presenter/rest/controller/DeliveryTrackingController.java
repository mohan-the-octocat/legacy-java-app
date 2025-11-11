package com.delivery.presenter.rest.controller;

import com.delivery.core.domain.model.DeliveryStatus;
import com.delivery.core.domain.model.DeliveryStatus.Location;
import com.delivery.core.usecases.TrackDeliveryUseCase;
import com.delivery.presenter.rest.dto.DeliveryStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/delivery")
public class DeliveryTrackingController {

    private final TrackDeliveryUseCase trackDeliveryUseCase;

    public DeliveryTrackingController(TrackDeliveryUseCase trackDeliveryUseCase) {
        this.trackDeliveryUseCase = trackDeliveryUseCase;
    }

    @GetMapping("/track/{orderId}")
    public ResponseEntity<DeliveryStatusResponse> trackDelivery(@PathVariable String orderId) {
        DeliveryStatus deliveryStatus = trackDeliveryUseCase.execute(orderId);

        DeliveryStatusResponse.Location responseLocation = null;
        if (deliveryStatus.getCurrentLocation() != null) {
            responseLocation = new DeliveryStatusResponse.Location(
                    deliveryStatus.getCurrentLocation().getLatitude(),
                    deliveryStatus.getCurrentLocation().getLongitude(),
                    deliveryStatus.getCurrentLocation().getAddress()
            );
        }

        DeliveryStatusResponse response = new DeliveryStatusResponse(
                deliveryStatus.getOrderId(),
                deliveryStatus.getStatus(),
                deliveryStatus.getEstimatedDeliveryTime(),
                deliveryStatus.getLastUpdated(),
                responseLocation
        );
        return ResponseEntity.ok(response);
    }
}