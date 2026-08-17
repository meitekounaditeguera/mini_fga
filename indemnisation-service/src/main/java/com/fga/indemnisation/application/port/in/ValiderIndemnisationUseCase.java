package com.fga.indemnisation.application.port.in;

import com.fga.indemnisation.domain.Indemnisation;

import java.math.BigDecimal;
import java.util.UUID;

public interface ValiderIndemnisationUseCase {

    Indemnisation validerIndemnisation(UUID indemnisationId, BigDecimal montant);
}