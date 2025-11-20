package com.delivery.data.db.jpa.entities;

import com.delivery.core.domain.Identity;
import com.delivery.core.domain.Subscription;
import com.delivery.core.domain.Subscription.SubscriptionStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import static com.delivery.data.db.jpa.entities.IdConverter.convertId;

@AllArgsConstructor
@Entity(name = "subscription")
@Getter
@NoArgsConstructor
@Setter
@Table(name = "subscription")
public class SubscriptionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerData customer;

    @ManyToOne
    @JoinColumn(name = "pg_location_id", nullable = false)
    private PGLocationData pgLocation;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    public static SubscriptionData from(Subscription subscription) {
        return new SubscriptionData(
                convertId(subscription.getId()),
                CustomerData.from(subscription.getCustomer()),
                PGLocationData.from(subscription.getPgLocation()),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getStatus()
        );
    }

    public Subscription fromThis() {
        return new Subscription(
                new Identity(id),
                customer.fromThis(),
                pgLocation.fromThis(),
                startDate,
                endDate,
                status
        );
    }
}
