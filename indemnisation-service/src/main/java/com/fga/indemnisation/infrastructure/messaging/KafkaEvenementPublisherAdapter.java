package com.fga.indemnisation.infrastructure.messaging;

import com.fga.indemnisation.application.port.out.EvenementPublisherPort;
import com.fga.indemnisation.domain.event.IndemnisationValideeEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Le symétrique de KafkaEvenementPublisherAdapter côté sinistre-service.
 * indemnisation-service devient ici PRODUCTEUR (sur le topic
 * indemnisation-validee), alors qu'il reste par ailleurs CONSOMMATEUR
 * du topic dossier-cree (via DossierCreeEventListener, dans le même
 * dossier infrastructure/messaging). Les deux rôles coexistent
 * naturellement dans le même microservice.
 */
@Component
public class KafkaEvenementPublisherAdapter implements EvenementPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topicIndemnisationValidee;

    public KafkaEvenementPublisherAdapter(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${fga.kafka.topic-indemnisation-validee}") String topicIndemnisationValidee) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicIndemnisationValidee = topicIndemnisationValidee;
    }

    @Override
    public void publierIndemnisationValidee(IndemnisationValideeEvent evenement) {
        kafkaTemplate.send(topicIndemnisationValidee, evenement.numeroDossier(), evenement);
    }
}