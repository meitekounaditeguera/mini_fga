package com.fga.sinistre.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Le repository Spring Data JPA "brut". Spring génère l'implémentation
 * automatiquement (findAll, findById, save, count, ...).
 */
public interface DossierJpaRepository extends JpaRepository<DossierEntity, UUID> {
}
