package com.fga.erp.infrastructure.messaging;

import com.fga.erp.application.port.in.EnregistrerEcritureUseCase;
import com.fga.erp.domain.event.BordereauRecuEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BordereauRecuEventListener {

    private final EnregistrerEcritureUseCase useCase;

    public BordereauRecuEventListener(EnregistrerEcritureUseCase useCase) {
        this.useCase = useCase;
    }

    @KafkaListener(topics = "${fga.kafka.topic-bordereau-recu}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "bordereauRecuContainerFactory")
    public void ecouter(BordereauRecuEvent evenement) {
        useCase.enregistrerPrimeRecue(evenement.bordereauId(), evenement.compagnieAssurance(), evenement.contributionFga());
    }
}
