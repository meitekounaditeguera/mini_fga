package com.fga.erp.infrastructure.messaging;

import com.fga.erp.domain.event.BordereauRecuEvent;
import com.fga.erp.domain.event.DossierCreeEvent;
import com.fga.erp.domain.event.IndemnisationValideeEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrairement à sinistre-service ou indemnisation-service (qui
 * n'écoutent qu'UN SEUL type d'événement chacun), ce microservice en
 * écoute TROIS différents sur trois topics distincts. Le mécanisme
 * simple qu'on utilisait avant (une seule propriété
 * "spring.json.value.default.type" dans application.yml) ne suffit
 * plus : il ne peut désigner qu'UNE seule classe cible pour tout le
 * monde.
 *
 * La solution : créer une "usine" de consommateurs (ConsumerFactory)
 * différente PAR TYPE D'ÉVÉNEMENT, chacune sachant désérialiser vers sa
 * propre classe. Chaque @KafkaListener précisera ensuite explicitement
 * quelle usine utiliser via l'attribut "containerFactory".
 */
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private <T> ConsumerFactory<String, T> creerConsumerFactory(Class<T> typeCible) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(typeCible);
        deserializer.ignoreTypeHeaders();
        deserializer.setRemoveTypeHeaders(true);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DossierCreeEvent> dossierCreeContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DossierCreeEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(creerConsumerFactory(DossierCreeEvent.class));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, IndemnisationValideeEvent> indemnisationValideeContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, IndemnisationValideeEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(creerConsumerFactory(IndemnisationValideeEvent.class));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BordereauRecuEvent> bordereauRecuContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BordereauRecuEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(creerConsumerFactory(BordereauRecuEvent.class));
        return factory;
    }
}
