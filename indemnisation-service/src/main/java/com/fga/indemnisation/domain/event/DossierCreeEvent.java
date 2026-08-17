package com.fga.indemnisation.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ATTENTION - point important à bien comprendre : cette classe est la
 * COPIE EXACTE de celle qu'on a écrite dans sinistre-service (même nom,
 * mêmes champs, dans le même package relatif domain/event).
 *
 * Ce n'est PAS un import ou un partage de code entre les deux microservices
 * - chaque microservice a SA PROPRE copie de cette classe. C'est volontaire :
 * en architecture microservices, on évite de partager du code Java entre
 * services (ça recréerait un couplage fort, l'inverse de ce qu'on cherche).
 *
 * Le seul "contrat" partagé entre les deux services est la STRUCTURE DU
 * JSON qui transite sur Kafka - pas une classe Java partagée. C'est ce
 * qui permet à chaque microservice d'évoluer indépendamment.
 *
 * Spring Kafka va utiliser cette classe pour désérialiser automatiquement
 * le JSON reçu depuis le topic "dossier-cree" en objet Java.
 */
public record DossierCreeEvent(
        UUID dossierId,
        String numeroDossier,
        String nomVictime,
        LocalDateTime dateEvenement
) {
}
