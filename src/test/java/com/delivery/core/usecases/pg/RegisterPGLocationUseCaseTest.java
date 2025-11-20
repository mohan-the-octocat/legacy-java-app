package com.delivery.core.usecases.pg;

import com.delivery.core.domain.DomainException;
import com.delivery.core.domain.PGLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegisterPGLocationUseCaseTest {

    @InjectMocks
    private RegisterPGLocationUseCase useCase;

    @Mock
    private PGLocationRepository repository;

    @Test
    public void executeRegistersNewPGLocation() {
        // given
        RegisterPGLocationUseCase.Input input = new RegisterPGLocationUseCase.Input(
                "Happy PG",
                "123 Street, Bangalore",
                "9999999999"
        );

        // and
        doReturn(Optional.empty())
                .when(repository)
                .findByName(input.getName());

        doReturn(PGLocation.newInstance(input.getName(), input.getAddress(), input.getManagerContact()))
                .when(repository)
                .save(any(PGLocation.class));

        // when
        RegisterPGLocationUseCase.Output output = useCase.execute(input);

        // then
        assertThat(output.getPgLocation().getName()).isEqualTo(input.getName());
        verify(repository).save(any(PGLocation.class));
    }

    @Test
    public void executeThrowsExceptionWhenPGLocationAlreadyExists() {
        // given
        RegisterPGLocationUseCase.Input input = new RegisterPGLocationUseCase.Input(
                "Existing PG",
                "Address",
                "Contact"
        );

        // and
        doReturn(Optional.of(PGLocation.newInstance("Existing PG", "Addr", "Cont")))
                .when(repository)
                .findByName(input.getName());

        // then
        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(DomainException.class)
                .hasMessage("PG Location with this name already exists");
    }
}
