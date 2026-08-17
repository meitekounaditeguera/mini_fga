package com.fga.sinistre.infrastructure.persistence;

import com.fga.sinistre.application.port.out.DossierRepositoryPort;
import com.fga.sinistre.domain.Dossier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * L'ADAPTATEUR : c'est le pont entre le monde du domaine (Dossier) et le
 * monde technique (DossierEntity + JPA). Il implémente le port de sortie
 * défini dans la couche application, et fait la conversion dans les 2 sens.
 *
 * C'est le seul endroit de tout le microservice où les deux mondes se
 * rencontrent.
 */
@Component
public class DossierRepositoryAdapter implements DossierRepositoryPort {

    private final DossierJpaRepository jpaRepository;

    public DossierRepositoryAdapter(DossierJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Dossier sauvegarder(Dossier dossier) {
        DossierEntity entity = versEntity(dossier);
        DossierEntity sauvegardee = jpaRepository.save(entity);
        return versDomaine(sauvegardee);
    }

    @Override
    public Optional<Dossier> trouverParId(UUID id) {
        return jpaRepository.findById(id).map(this::versDomaine);
    }

    @Override
    public List<Dossier> listerTous() {
        return jpaRepository.findAll().stream()
                .map(this::versDomaine)
                .toList();
    }

    private DossierEntity versEntity(Dossier dossier) {
        return new DossierEntity(
                dossier.getId(),
                dossier.getNumero(),
                dossier.getNomVictime(),
                dossier.getDateAccident(),
                dossier.getLieu(),
                dossier.getStatut(),
                dossier.getDateCreation()
        );
    }

    private Dossier versDomaine(DossierEntity entity) {
        return Dossier.reconstituer(
                entity.getId(),
                entity.getNumero(),
                entity.getNomVictime(),
                entity.getDateAccident(),
                entity.getLieu(),
                entity.getStatut(),
                entity.getDateCreation()
        );
    }
}
