package com.fga.indemnisation.application.port.in;

import com.fga.indemnisation.domain.Indemnisation;

import java.util.UUID;

public interface ConsulterIndemnisationParDossierUseCase {

    Indemnisation consulterParDossierId(UUID dossierId);
}