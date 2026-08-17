package com.fga.sinistre.infrastructure.messaging;

import com.fga.sinistre.application.port.out.EvenementPublisherPort;
import com.fga.sinistre.domain.event.DossierCreeEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * L'ADAPTATEUR Kafka : l'équivalent de DossierRepositoryAdapter, mais pour
 * la messagerie au lieu de la base de données.
 *
 * C'est le SEUL endroit de tout le microservice où Kafka est mentionné
 * explicitement. Si demain on doit remplacer Kafka par un autre outil,
 * c'est cette classe (et elle seule) qu'on réécrit.
 */
@Component
public class KafkaEvenementPublisherAdapter implements EvenementPublisherPort {

    // KafkaTemplate : fourni automatiquement par Spring Kafka (grâce à la
    // dépendance spring-kafka + la config bootstrap-servers dans
    // application.yml). C'est l'outil concret qui sait parler au broker Kafka.
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Le nom du topic est lu depuis application.yml (fga.kafka.topic-dossier-cree)
    // grâce à @Value, plutôt que codé "en dur" ici.
    private final String topicDossierCree;

    public KafkaEvenementPublisherAdapter(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${fga.kafka.topic-dossier-cree}") String topicDossierCree) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicDossierCree = topicDossierCree;
    }

    @Override
    public void publierDossierCree(DossierCreeEvent evenement) {
        // On utilise le numéro de dossier comme "clé" du message Kafka.
        // Ça garantit que tous les événements concernant le MÊME dossier
        // arrivent toujours dans l'ordre chez les consommateurs (Kafka
        // garantit l'ordre uniquement pour les messages ayant la même clé).
        kafkaTemplate.send(topicDossierCree, evenement.numeroDossier(), evenement);
    }
}