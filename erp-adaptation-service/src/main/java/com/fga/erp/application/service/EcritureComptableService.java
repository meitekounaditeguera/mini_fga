package com.fga.erp.application.service;

import com.fga.erp.application.port.in.ConsulterSyntheseUseCase;
import com.fga.erp.application.port.in.EnregistrerEcritureUseCase;
import com.fga.erp.application.port.out.EcritureRepositoryPort;
import com.fga.erp.domain.EcritureComptable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class EcritureComptableService implements EnregistrerEcritureUseCase, ConsulterSyntheseUseCase {

    private final EcritureRepositoryPort repository;

    public EcritureComptableService(EcritureRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void enregistrerOuvertureDossier(UUID dossierId, String numeroDossier) {
        repository.sauvegarder(EcritureComptable.dossierOuvert(numeroDossier));
    }

    @Override
    public void enregistrerIndemnisationVersee(UUID indemnisationId, String numeroDossier, BigDecimal montant) {
        repository.sauvegarder(EcritureComptable.indemnisationVersee(numeroDossier, montant));
    }

    @Override
    public void enregistrerPrimeRecue(UUID bordereauId, String compagnieAssurance, BigDecimal contributionFga) {
        repository.sauvegarder(EcritureComptable.primeRecue(compagnieAssurance, contributionFga));
    }

    @Override
    public List<EcritureComptable> listerEcritures() {
        return repository.listerToutes();
    }
}
