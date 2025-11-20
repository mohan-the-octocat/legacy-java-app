package com.delivery.core.usecases.subscription;

import com.delivery.core.domain.*;
import com.delivery.core.usecases.customer.CustomerRepository;
import com.delivery.core.usecases.pg.PGLocationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CreateSubscriptionUseCaseTest {

    @InjectMocks
    private CreateSubscriptionUseCase useCase;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PGLocationRepository pgLocationRepository;

    @Test
    public void executeCreatesSubscription() {
        // given
        Identity customerId = Identity.newIdentity();
        CreateSubscriptionUseCase.Input input = new CreateSubscriptionUseCase.Input(
                customerId,
                "Happy PG",
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        );

        Customer customer = mock(Customer.class);
        PGLocation pgLocation = mock(PGLocation.class);

        // and
        doReturn(Optional.of(customer))
                .when(customerRepository)
                .findByIdentity(customerId);

        doReturn(Optional.of(pgLocation))
                .when(pgLocationRepository)
                .findByName(input.getPgLocationName());

        doReturn(Subscription.newInstance(customer, pgLocation, input.getStartDate(), input.getEndDate()))
                .when(subscriptionRepository)
                .save(any(Subscription.class));

        // when
        CreateSubscriptionUseCase.Output output = useCase.execute(input);

        // then
        assertThat(output.getSubscription()).isNotNull();
        assertThat(output.getSubscription().getStatus()).isEqualTo(Subscription.SubscriptionStatus.ACTIVE);
    }

    @Test
    public void executeThrowsExceptionWhenCustomerNotFound() {
        // given
        CreateSubscriptionUseCase.Input input = new CreateSubscriptionUseCase.Input(
                Identity.newIdentity(),
                "Happy PG",
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        );

        // and
        doReturn(Optional.empty())
                .when(customerRepository)
                .findByIdentity(input.getCustomerId());

        // then
        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");
    }
}
