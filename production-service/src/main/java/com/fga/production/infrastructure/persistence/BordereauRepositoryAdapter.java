package com.fga.production.infrastructure.persistence;

import com.fga.production.application.port.out.BordereauRepositoryPort;
import com.fga.production.domain.BordereauPrime;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BordereauRepositoryAdapter implements BordereauRepositoryPort {

    private final BordereauJpaRepository jpaRepository;

    public BordereauRepositoryAdapter(BordereauJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public BordereauPrime sauvegarder(BordereauPrime bordereau) {
        BordereauEntity entity = versEntity(bordereau);
        BordereauEntity sauvegarde = jpaRepository.save(entity);
        return versDomaine(sauvegarde);
    }

    @Override
    public List<BordereauPrime> listerTous() {
        return jpaRepository.findAll().stream().map(this::versDomaine).toList();
    }

    private BordereauEntity versEntity(BordereauPrime bordereau) {
        return new BordereauEntity(bordereau.getId(), bordereau.getCompagnieAssurance(),
                bordereau.getMontantPrimesCollectees(), bordereau.getContributionFga(),
                bordereau.getStatut(), bordereau.getDateReception());
    }

    private BordereauPrime versDomaine(BordereauEntity entity) {
        return BordereauPrime.reconstituer(entity.getId(), entity.getCompagnieAssurance(),
                entity.getMontantPrimesCollectees(), entity.getContributionFga(),
                entity.getStatut(), entity.getDateReception());
    }
}
