package com.fga.sinistre.application.port.in;

import com.fga.sinistre.domain.Dossier;

import java.util.List;
import java.util.UUID;

public interface ConsulterDossiersUseCase {

    List<Dossier> listerDossiers();

    Dossier consulterDossier(UUID id);
}
