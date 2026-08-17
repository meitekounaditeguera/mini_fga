package com.fga.indemnisation.application.port.out;

import com.fga.indemnisation.domain.event.IndemnisationValideeEvent;

/**
 * Jusqu'ici, indemnisation-service n'avait QUE des ports pour ÉCOUTER
 * Kafka (via le listener) - jamais pour publier. C'est le premier port
 * de sortie "publication" de ce microservice, symétrique de
 * EvenementPublisherPort qu'on avait créé côté sinistre-service.
 */
public interface EvenementPublisherPort {

    void publierIndemnisationValidee(IndemnisationValideeEvent evenement);
}