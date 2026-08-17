package com.fga.indemnisation.infrastructure.messaging;

import com.fga.indemnisation.application.port.in.TraiterDossierCreeUseCase;
import com.fga.indemnisation.domain.event.DossierCreeEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * LE LISTENER : l'équivalent, côté lecture, de KafkaEvenementPublisherAdapter
 * côté sinistre-service. C'est le SEUL endroit de ce microservice où Kafka
 * est mentionné explicitement.
 *
 * L'annotation @KafkaListener fait tout le travail difficile : Spring va,
 * en arrière-plan, rester connecté en permanence au topic indiqué, et
 * appeler automatiquement cette méthode CHAQUE FOIS qu'un nouveau message
 * y est publié - même si l'application vient tout juste de démarrer et que
 * des messages étaient déjà en attente depuis longtemps.
 */
@Component
public class DossierCreeEventListener {

    private final TraiterDossierCreeUseCase traiterDossierCreeUseCase;

    public DossierCreeEventListener(TraiterDossierCreeUseCase traiterDossierCreeUseCase) {
        this.traiterDossierCreeUseCase = traiterDossierCreeUseCase;
    }

    @KafkaListener(topics = "${fga.kafka.topic-dossier-cree}", groupId = "${spring.kafka.consumer.group-id}")
    public void ecouterDossierCree(DossierCreeEvent evenement) {
        traiterDossierCreeUseCase.traiterDossierCree(evenement);
    }
}