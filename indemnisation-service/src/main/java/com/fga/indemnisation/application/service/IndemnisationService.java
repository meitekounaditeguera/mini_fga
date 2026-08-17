package com.fga.indemnisation.application.service;

import com.fga.indemnisation.application.port.in.ConsulterIndemnisationParDossierUseCase;
import com.fga.indemnisation.application.port.in.TraiterDossierCreeUseCase;
import com.fga.indemnisation.application.port.in.ValiderIndemnisationUseCase;
import com.fga.indemnisation.application.port.out.EvenementPublisherPort;
import com.fga.indemnisation.application.port.out.IndemnisationRepositoryPort;
import com.fga.indemnisation.domain.Indemnisation;
import com.fga.indemnisation.domain.event.DossierCreeEvent;
import com.fga.indemnisation.domain.event.IndemnisationValideeEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class IndemnisationService implements TraiterDossierCreeUseCase, ConsulterIndemnisationParDossierUseCase,
        ValiderIndemnisationUseCase {

    private final IndemnisationRepositoryPort indemnisationRepository;
    private final EvenementPublisherPort evenementPublisher;

    public IndemnisationService(IndemnisationRepositoryPort indemnisationRepository,
                                 EvenementPublisherPort evenementPublisher) {
        this.indemnisationRepository = indemnisationRepository;
        this.evenementPublisher = evenementPublisher;
    }

    @Override
    public void traiterDossierCree(DossierCreeEvent evenement) {
        Indemnisation indemnisation = Indemnisation.creerEnAttente(
                evenement.dossierId(),
                evenement.numeroDossier()
        );
        indemnisationRepository.sauvegarder(indemnisation);
    }

    @Override
    public Indemnisation consulterParDossierId(UUID dossierId) {
        return indemnisationRepository.trouverParDossierId(dossierId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Aucune indemnisation trouvée pour le dossier : " + dossierId));
    }

    @Override
    public Indemnisation validerIndemnisation(UUID indemnisationId, BigDecimal montant) {
        Indemnisation indemnisation = indemnisationRepository.trouverParId(indemnisationId)
                .orElseThrow(() -> new NoSuchElementException("Indemnisation introuvable : " + indemnisationId));

        // C'est ICI que la règle métier s'applique - la méthode valider()
        // qu'on avait écrite dès le tout premier jour, jamais utilisée
        // jusqu'à maintenant. Si l'indemnisation n'est pas EN_ATTENTE,
        // une IllegalStateException sera levée ici, gérée ensuite par
        // GlobalExceptionHandler (renvoyée en 400 au client).
        indemnisation.valider(montant);

        Indemnisation indemnisationSauvegardee = indemnisationRepository.sauvegarder(indemnisation);

        // Même principe que dans sinistre-service : on publie APRÈS la
        // sauvegarde réussie, jamais avant.
        IndemnisationValideeEvent evenement = IndemnisationValideeEvent.depuis(
                indemnisationSauvegardee.getId(),
                indemnisationSauvegardee.getDossierId(),
                indemnisationSauvegardee.getNumeroDossier(),
                indemnisationSauvegardee.getMontant()
        );
        evenementPublisher.publierIndemnisationValidee(evenement);

        return indemnisationSauvegardee;
    }
}