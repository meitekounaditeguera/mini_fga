package com.fga.erp.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

// Copie locale, comme entre sinistre-service et indemnisation-service -
// erp-adaptation-service ne partage aucun code avec les autres.
public record DossierCreeEvent(UUID dossierId, String numeroDossier, String nomVictime, LocalDateTime dateEvenement) {}
