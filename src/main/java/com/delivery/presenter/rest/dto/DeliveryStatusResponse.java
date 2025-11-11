package com.delivery.presenter.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusResponse {
    private String orderId;
    private String deliveryStatus;
    private OffsetDateTime estimatedDeliveryTime;
    private OffsetDateTime lastUpdated;
    private Location currentLocation;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private Double latitude;
        private Double longitude;
        private String address;
    }
}