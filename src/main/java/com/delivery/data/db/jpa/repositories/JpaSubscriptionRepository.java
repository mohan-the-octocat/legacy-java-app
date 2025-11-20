package com.delivery.data.db.jpa.repositories;

import com.delivery.data.db.jpa.entities.SubscriptionData;
import com.delivery.core.domain.Subscription.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionData, Long> {
    List<SubscriptionData> findByStatus(SubscriptionStatus status);
}
