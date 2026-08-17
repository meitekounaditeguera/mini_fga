package com.fga.production.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * L'événement publié par production-service, sur le topic
 * "bordereau-recu". C'est le 3e "point d'entrée" d'événement dans notre
 * système - erp-adaptation-service va l'écouter en plus des deux autres
 * déjà existants.
 */
public record BordereauRecuEvent(
        UUID bordereauId,
        String compagnieAssurance,
        BigDecimal montantPrimesCollectees,
        BigDecimal contributionFga,
        LocalDateTime dateEvenement
) {
    public static BordereauRecuEvent depuis(UUID bordereauId, String compagnieAssurance,
                                             BigDecimal montantPrimesCollectees, BigDecimal contributionFga) {
        return new BordereauRecuEvent(bordereauId, compagnieAssurance, montantPrimesCollectees,
                contributionFga, LocalDateTime.now());
    }
}
