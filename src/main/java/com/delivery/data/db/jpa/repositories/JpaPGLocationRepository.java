package com.delivery.data.db.jpa.repositories;

import com.delivery.data.db.jpa.entities.PGLocationData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaPGLocationRepository extends JpaRepository<PGLocationData, Long> {
    Optional<PGLocationData> findByName(String name);
}
