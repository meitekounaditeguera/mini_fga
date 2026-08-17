package com.fga.erp.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record IndemnisationValideeEvent(UUID indemnisationId, UUID dossierId, String numeroDossier,
                                          BigDecimal montant, LocalDateTime dateEvenement) {}
