package com.fga.sinistre.application.port.out;

import com.fga.sinistre.domain.event.DossierCreeEvent;

/**
 * Port de SORTIE : comme DossierRepositoryPort, mais pour publier des
 * événements au lieu de sauvegarder en base.
 *
 * DossierService (la couche application) va dépendre de CETTE interface,
 * jamais directement de Kafka. Si demain on remplace Kafka par RabbitMQ
 * (un autre outil de messagerie), seule l'implémentation change - pas
 * une seule ligne de DossierService à modifier.
 */
public interface EvenementPublisherPort {

    void publierDossierCree(DossierCreeEvent evenement);
}