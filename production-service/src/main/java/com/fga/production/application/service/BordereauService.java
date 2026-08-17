package com.fga.production.application.service;

import com.fga.production.application.port.in.ConsulterBordereauxUseCase;
import com.fga.production.application.port.in.ReceptionnerBordereauUseCase;
import com.fga.production.application.port.out.BordereauRepositoryPort;
import com.fga.production.application.port.out.EvenementPublisherPort;
import com.fga.production.domain.BordereauPrime;
import com.fga.production.domain.event.BordereauRecuEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BordereauService implements ReceptionnerBordereauUseCase, ConsulterBordereauxUseCase {

    private final BordereauRepositoryPort repository;
    private final EvenementPublisherPort evenementPublisher;

    public BordereauService(BordereauRepositoryPort repository, EvenementPublisherPort evenementPublisher) {
        this.repository = repository;
        this.evenementPublisher = evenementPublisher;
    }

    @Override
    public BordereauPrime receptionnerBordereau(String compagnieAssurance, BigDecimal montantPrimesCollectees) {
        BordereauPrime bordereau = BordereauPrime.recevoir(compagnieAssurance, montantPrimesCollectees);
        BordereauPrime sauvegarde = repository.sauvegarder(bordereau);

        BordereauRecuEvent evenement = BordereauRecuEvent.depuis(
                sauvegarde.getId(), sauvegarde.getCompagnieAssurance(),
                sauvegarde.getMontantPrimesCollectees(), sauvegarde.getContributionFga()
        );
        evenementPublisher.publierBordereauRecu(evenement);

        return sauvegarde;
    }

    @Override
    public List<BordereauPrime> listerBordereaux() {
        return repository.listerTous();
    }
}
