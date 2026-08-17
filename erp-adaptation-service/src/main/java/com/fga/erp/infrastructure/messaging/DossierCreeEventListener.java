package com.fga.erp.infrastructure.messaging;

import com.fga.erp.application.port.in.EnregistrerEcritureUseCase;
import com.fga.erp.domain.event.DossierCreeEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DossierCreeEventListener {

    private final EnregistrerEcritureUseCase useCase;

    public DossierCreeEventListener(EnregistrerEcritureUseCase useCase) {
        this.useCase = useCase;
    }

    @KafkaListener(topics = "${fga.kafka.topic-dossier-cree}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "dossierCreeContainerFactory")
    public void ecouter(DossierCreeEvent evenement) {
        useCase.enregistrerOuvertureDossier(evenement.dossierId(), evenement.numeroDossier());
    }
}
