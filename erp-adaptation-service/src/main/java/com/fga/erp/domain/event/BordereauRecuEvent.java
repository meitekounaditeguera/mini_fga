package com.fga.erp.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BordereauRecuEvent(UUID bordereauId, String compagnieAssurance,
                                   BigDecimal montantPrimesCollectees, BigDecimal contributionFga,
                                   LocalDateTime dateEvenement) {}
