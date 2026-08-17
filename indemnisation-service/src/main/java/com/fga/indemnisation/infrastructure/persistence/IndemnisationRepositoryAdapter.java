package com.fga.indemnisation.infrastructure.persistence;

import com.fga.indemnisation.application.port.out.IndemnisationRepositoryPort;
import com.fga.indemnisation.domain.Indemnisation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class IndemnisationRepositoryAdapter implements IndemnisationRepositoryPort {

    private final IndemnisationJpaRepository jpaRepository;

    public IndemnisationRepositoryAdapter(IndemnisationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Indemnisation sauvegarder(Indemnisation indemnisation) {
        IndemnisationEntity entity = versEntity(indemnisation);
        IndemnisationEntity sauvegardee = jpaRepository.save(entity);
        return versDomaine(sauvegardee);
    }

    @Override
    public Optional<Indemnisation> trouverParId(UUID id) {
        return jpaRepository.findById(id).map(this::versDomaine);
    }

    @Override
    public Optional<Indemnisation> trouverParDossierId(UUID dossierId) {
        return jpaRepository.findByDossierId(dossierId).map(this::versDomaine);
    }

    @Override
    public List<Indemnisation> listerToutes() {
        return jpaRepository.findAll().stream()
                .map(this::versDomaine)
                .toList();
    }

    private IndemnisationEntity versEntity(Indemnisation indemnisation) {
        return new IndemnisationEntity(
                indemnisation.getId(),
                indemnisation.getDossierId(),
                indemnisation.getNumeroDossier(),
                indemnisation.getMontant(),
                indemnisation.getStatut(),
                indemnisation.getDateCreation(),
                indemnisation.getDateValidation()
        );
    }

    private Indemnisation versDomaine(IndemnisationEntity entity) {
        return Indemnisation.reconstituer(
                entity.getId(),
                entity.getDossierId(),
                entity.getNumeroDossier(),
                entity.getMontant(),
                entity.getStatut(),
                entity.getDateCreation(),
                entity.getDateValidation()
        );
    }
}