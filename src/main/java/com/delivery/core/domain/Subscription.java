package com.delivery.core.domain;

import lombok.Value;
import java.time.LocalDate;

@Value
public class Subscription {
    private final Identity id;
    private final Customer customer;
    private final PGLocation pgLocation;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final SubscriptionStatus status;

    public enum SubscriptionStatus {
        ACTIVE,
        EXPIRED,
        CANCELLED
    }

    public static Subscription newInstance(Customer customer, PGLocation pgLocation, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new DomainException("End date must be after start date");
        }
        return new Subscription(
            Identity.newIdentity(),
            customer,
            pgLocation,
            startDate,
            endDate,
            SubscriptionStatus.ACTIVE
        );
    }
    
    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE && 
               !LocalDate.now().isBefore(startDate) && 
               !LocalDate.now().isAfter(endDate);
    }
}
