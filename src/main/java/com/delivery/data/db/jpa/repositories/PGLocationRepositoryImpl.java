package com.delivery.data.db.jpa.repositories;

import com.delivery.core.domain.PGLocation;
import com.delivery.core.usecases.pg.PGLocationRepository;
import com.delivery.data.db.jpa.entities.PGLocationData;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class PGLocationRepositoryImpl implements PGLocationRepository {
    private final JpaPGLocationRepository repository;

    public PGLocationRepositoryImpl(JpaPGLocationRepository repository) {
        this.repository = repository;
    }

    @Override
    public PGLocation save(PGLocation pgLocation) {
        PGLocationData data = PGLocationData.from(pgLocation);
        return repository.save(data).fromThis();
    }

    @Override
    public Optional<PGLocation> findByName(String name) {
        return repository.findByName(name).map(PGLocationData::fromThis);
    }
}
