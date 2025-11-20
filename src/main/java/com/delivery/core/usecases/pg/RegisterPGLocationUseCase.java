package com.delivery.core.usecases.pg;

import com.delivery.core.domain.DomainException;
import com.delivery.core.domain.PGLocation;
import com.delivery.core.usecases.UseCase;
import lombok.Value;

public class RegisterPGLocationUseCase extends UseCase<RegisterPGLocationUseCase.Input, RegisterPGLocationUseCase.Output> {
    private final PGLocationRepository repository;

    public RegisterPGLocationUseCase(PGLocationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Output execute(Input input) {
        if (repository.findByName(input.name).isPresent()) {
            throw new DomainException("PG Location with this name already exists");
        }

        PGLocation pgLocation = PGLocation.newInstance(input.name, input.address, input.managerContact);
        PGLocation saved = repository.save(pgLocation);

        return new Output(saved);
    }

    @Value
    public static class Input implements UseCase.InputValues {
        String name;
        String address;
        String managerContact;
    }

    @Value
    public static class Output implements UseCase.OutputValues {
        PGLocation pgLocation;
    }
}
