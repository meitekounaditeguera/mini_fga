package com.fga.production.infrastructure.messaging;

import com.fga.production.application.port.out.EvenementPublisherPort;
import com.fga.production.domain.event.BordereauRecuEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEvenementPublisherAdapter implements EvenementPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public KafkaEvenementPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate,
                                           @Value("${fga.kafka.topic-bordereau-recu}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publierBordereauRecu(BordereauRecuEvent evenement) {
        kafkaTemplate.send(topic, evenement.bordereauId().toString(), evenement);
    }
}
