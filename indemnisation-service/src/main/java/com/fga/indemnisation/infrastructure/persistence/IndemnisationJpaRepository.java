package com.fga.indemnisation.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IndemnisationJpaRepository extends JpaRepository<IndemnisationEntity, UUID> {

    Optional<IndemnisationEntity> findByDossierId(UUID dossierId);
}