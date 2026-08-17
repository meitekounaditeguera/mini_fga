package com.fga.erp.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EcritureJpaRepository extends JpaRepository<EcritureEntity, UUID> {
}
