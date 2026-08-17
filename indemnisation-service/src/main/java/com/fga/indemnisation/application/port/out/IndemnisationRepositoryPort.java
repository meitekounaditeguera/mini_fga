package com.fga.indemnisation.application.port.out;

import com.fga.indemnisation.domain.Indemnisation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IndemnisationRepositoryPort {

    Indemnisation sauvegarder(Indemnisation indemnisation);

    Optional<Indemnisation> trouverParId(UUID id);

    // Nouvelle méthode : nécessaire pour répondre à la question "quelle est
    // l'indemnisation liée à CE dossier précis" - la clé de recherche n'est
    // pas l'id de l'indemnisation elle-même, mais le dossierId qu'elle référence.
    Optional<Indemnisation> trouverParDossierId(UUID dossierId);

    List<Indemnisation> listerToutes();
}