package com.fga.erp.infrastructure.messaging;

import com.fga.erp.application.port.in.EnregistrerEcritureUseCase;
import com.fga.erp.domain.event.IndemnisationValideeEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IndemnisationValideeEventListener {

    private final EnregistrerEcritureUseCase useCase;

    public IndemnisationValideeEventListener(EnregistrerEcritureUseCase useCase) {
        this.useCase = useCase;
    }

    @KafkaListener(topics = "${fga.kafka.topic-indemnisation-validee}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "indemnisationValideeContainerFactory")
    public void ecouter(IndemnisationValideeEvent evenement) {
        useCase.enregistrerIndemnisationVersee(evenement.indemnisationId(), evenement.numeroDossier(), evenement.montant());
    }
}
