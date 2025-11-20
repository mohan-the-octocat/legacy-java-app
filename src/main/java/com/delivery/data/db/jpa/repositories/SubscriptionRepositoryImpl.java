package com.delivery.data.db.jpa.repositories;

import com.delivery.core.domain.Subscription;
import com.delivery.core.usecases.subscription.SubscriptionRepository;
import com.delivery.data.db.jpa.entities.SubscriptionData;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final JpaSubscriptionRepository repository;

    public SubscriptionRepositoryImpl(JpaSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionData data = SubscriptionData.from(subscription);
        return repository.save(data).fromThis();
    }

    @Override
    public List<Subscription> findAllActive() {
        return repository.findByStatus(Subscription.SubscriptionStatus.ACTIVE)
                .stream()
                .map(SubscriptionData::fromThis)
                .collect(Collectors.toList());
    }
}
