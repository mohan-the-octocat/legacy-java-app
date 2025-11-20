package com.delivery.core.usecases.pg;

import com.delivery.core.domain.PGLocation;
import java.util.Optional;

public interface PGLocationRepository {
    PGLocation save(PGLocation pgLocation);
    Optional<PGLocation> findByName(String name);
}
