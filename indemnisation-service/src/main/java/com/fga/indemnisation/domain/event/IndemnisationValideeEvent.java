package com.fga.indemnisation.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Le 2e événement de toute la chaîne (après DossierCreeEvent). Cette fois,
 * c'est indemnisation-service qui devient PRODUCTEUR (rappelle-toi : un
 * même service peut être consommateur d'un événement et producteur d'un
 * autre, ce n'est jamais un rôle figé).
 *
 * recouvrement-service (le futur microservice Python) va écouter ce
 * topic pour savoir quand récupérer sa contribution.
 */
public record IndemnisationValideeEvent(
        UUID indemnisationId,
        UUID dossierId,
        String numeroDossier,
        BigDecimal montant,
        LocalDateTime dateEvenement
) {

    public static IndemnisationValideeEvent depuis(UUID indemnisationId, UUID dossierId,
                                                     String numeroDossier, BigDecimal montant) {
        return new IndemnisationValideeEvent(indemnisationId, dossierId, numeroDossier, montant, LocalDateTime.now());
    }
}