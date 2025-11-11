package com.delivery.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatus {
    private String orderId;
    private String status; // e.g., "Processing", "Shipped", "Out for Delivery", "Delivered", "Cancelled", "Returned"
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