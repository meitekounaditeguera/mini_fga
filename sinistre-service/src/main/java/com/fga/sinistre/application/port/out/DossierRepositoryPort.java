package com.fga.sinistre.application.port.out;

import com.fga.sinistre.domain.Dossier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port de SORTIE : le use case (couche application) dépend de cette interface,
 * mais ne sait pas COMMENT les dossiers sont réellement stockés.
 *
 * C'est la couche infrastructure qui fournira l'implémentation concrète
 * (avec PostgreSQL, via JPA). Ça permet, en théorie, de changer de base de
 * données sans toucher une seule ligne de logique métier.
 */
public interface DossierRepositoryPort {

    Dossier sauvegarder(Dossier dossier);

    Optional<Dossier> trouverParId(UUID id);

    List<Dossier> listerTous();
}
