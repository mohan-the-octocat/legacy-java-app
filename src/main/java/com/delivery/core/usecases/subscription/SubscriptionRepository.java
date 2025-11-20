package com.delivery.core.usecases.subscription;

import com.delivery.core.domain.Subscription;
import java.util.List;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    List<Subscription> findAllActive();
}
