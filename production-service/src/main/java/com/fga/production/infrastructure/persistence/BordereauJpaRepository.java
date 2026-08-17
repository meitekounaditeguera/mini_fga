package com.fga.production.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BordereauJpaRepository extends JpaRepository<BordereauEntity, UUID> {
}
