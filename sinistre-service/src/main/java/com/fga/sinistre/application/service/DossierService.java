package com.fga.sinistre.application.service;

import com.fga.sinistre.application.port.in.ConsulterDossiersUseCase;
import com.fga.sinistre.application.port.in.CreerDossierUseCase;
import com.fga.sinistre.application.port.out.DossierRepositoryPort;
import com.fga.sinistre.application.port.out.EvenementPublisherPort;
import com.fga.sinistre.domain.Dossier;
import com.fga.sinistre.domain.event.DossierCreeEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * L'orchestrateur (couche application). C'est ici qu'on :
 *  1. appelle le domaine pour appliquer les règles métier,
 *  2. sauvegarde via le port de sortie,
 *  3. publie un événement Kafka pour prévenir les autres microservices.
 *
 * Il implémente les deux ports d'entrée : c'est le point de contact unique
 * pour la couche presentation.
 */
@Service
public class DossierService implements CreerDossierUseCase, ConsulterDossiersUseCase {

    private final DossierRepositoryPort dossierRepository;
    private final EvenementPublisherPort evenementPublisher;

    public DossierService(DossierRepositoryPort dossierRepository,
                           EvenementPublisherPort evenementPublisher) {
        this.dossierRepository = dossierRepository;
        this.evenementPublisher = evenementPublisher;
    }

    @Override
    public Dossier creerDossier(String nomVictime, LocalDate dateAccident, String lieu) {
        String numero = genererNumero();
        Dossier dossier = Dossier.ouvrir(numero, nomVictime, dateAccident, lieu);
        Dossier dossierSauvegarde = dossierRepository.sauvegarder(dossier);

        // On publie l'événement APRÈS la sauvegarde réussie, jamais avant.
        // Si la sauvegarde échoue (ex: erreur de base de données), on ne veut
        // surtout pas prévenir indemnisation-service d'un dossier qui n'existe
        // finalement pas.
        DossierCreeEvent evenement = DossierCreeEvent.depuis(
                dossierSauvegarde.getId(),
                dossierSauvegarde.getNumero(),
                dossierSauvegarde.getNomVictime()
        );
        evenementPublisher.publierDossierCree(evenement);

        return dossierSauvegarde;
    }

    @Override
    public List<Dossier> listerDossiers() {
        return dossierRepository.listerTous();
    }

    @Override
    public Dossier consulterDossier(UUID id) {
        return dossierRepository.trouverParId(id)
                .orElseThrow(() -> new NoSuchElementException("Dossier introuvable : " + id));
    }

    /**
     * Génère un numéro de dossier au format SIN-AAAA-NNNN, comme dans le
     * vrai applicatif FGA (numéro de sinistre généré automatiquement).
     *
     * Basé sur le PLUS GRAND numéro déjà utilisé cette année (+1), plutôt
     * que sur un simple compte de lignes : compterDossiers()+1 générait un
     * doublon dès qu'un numéro manquait dans la séquence (dossier supprimé,
     * import partiel...), ce qui provoquait une violation de contrainte
     * unique en base à la création suivante.
     */
    private String genererNumero() {
        int annee = Year.now().getValue();
        String prefixe = "SIN-" + annee + "-";

        long dernierNumero = dossierRepository.listerTous().stream()
                .map(Dossier::getNumero)
                .filter(numero -> numero.startsWith(prefixe))
                .map(numero -> numero.substring(prefixe.length()))
                .mapToLong(suffixe -> {
                    try {
                        return Long.parseLong(suffixe);
                    } catch (NumberFormatException e) {
                        return 0L;
                    }
                })
                .max()
                .orElse(0L);

        return String.format("%s%04d", prefixe, dernierNumero + 1);
    }
}