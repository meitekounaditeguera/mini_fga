package com.fga.indemnisation.application.port.in;

import com.fga.indemnisation.domain.event.DossierCreeEvent;

/**
 * Port d'ENTRÉE : le contrat que le listener Kafka va appeler.
 *
 * Remarque : contrairement à CreerDossierUseCase (dans sinistre-service),
 * qui était appelé par un CONTROLLER REST, celui-ci sera appelé par un
 * LISTENER KAFKA. Le principe reste identique : la couche application ne
 * sait pas QUI l'appelle (HTTP ou Kafka), elle expose juste un contrat.
 */
public interface TraiterDossierCreeUseCase {

    void traiterDossierCree(DossierCreeEvent evenement);
}