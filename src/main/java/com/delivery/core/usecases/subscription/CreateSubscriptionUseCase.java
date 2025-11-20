package com.delivery.core.usecases.subscription;

import com.delivery.core.domain.*;
import com.delivery.core.usecases.UseCase;
import com.delivery.core.usecases.customer.CustomerRepository;
import com.delivery.core.usecases.pg.PGLocationRepository;
import lombok.Value;

import java.time.LocalDate;

public class CreateSubscriptionUseCase extends UseCase<CreateSubscriptionUseCase.Input, CreateSubscriptionUseCase.Output> {
    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final PGLocationRepository pgLocationRepository;

    public CreateSubscriptionUseCase(SubscriptionRepository subscriptionRepository,
                                     CustomerRepository customerRepository,
                                     PGLocationRepository pgLocationRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.customerRepository = customerRepository;
        this.pgLocationRepository = pgLocationRepository;
    }

    @Override
    public Output execute(Input input) {
        Customer customer = customerRepository.findByIdentity(input.customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        PGLocation pgLocation = pgLocationRepository.findByName(input.pgLocationName)
                .orElseThrow(() -> new NotFoundException("PG Location not found"));

        Subscription subscription = Subscription.newInstance(
                customer,
                pgLocation,
                input.startDate,
                input.endDate
        );

        Subscription saved = subscriptionRepository.save(subscription);

        return new Output(saved);
    }

    @Value
    public static class Input implements UseCase.InputValues {
        Identity customerId;
        String pgLocationName;
        LocalDate startDate;
        LocalDate endDate;
    }

    @Value
    public static class Output implements UseCase.OutputValues {
        Subscription subscription;
    }
}
