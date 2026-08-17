package com.fga.indemnisation.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * L'indemnisation, telle que définie par le métier.
 *
 * RAPPEL DU PRINCIPE (comme pour Dossier.java dans sinistre-service) :
 * cette classe ne connaît NI Spring, NI JPA, NI PostgreSQL. Elle ne contient
 * QUE de la logique métier pure. On doit pouvoir la tester avec un simple
 * "new Indemnisation(...)" dans un test, sans base de données, sans serveur.
 *
 * C'est ce qui permet, dans une vraie architecture microservices, de tester
 * les règles métier très rapidement et de changer la techno de stockage
 * sans jamais toucher à cette classe.
 */
public class Indemnisation {

    private final UUID id;

    // Le lien avec le dossier de sinistre (créé dans l'AUTRE microservice,
    // sinistre-service). On ne stocke PAS l'objet Dossier entier ici -
    // seulement son identifiant. C'est une règle importante des
    // microservices : chaque service ne connaît des autres QUE ce qui lui
    // est strictement nécessaire (ici, juste l'id pour faire le lien).
    private final UUID dossierId;

    private final String numeroDossier;

    // BigDecimal et pas "double" : en informatique, on n'utilise JAMAIS
    // double/float pour représenter de l'argent, à cause des erreurs
    // d'arrondi. BigDecimal garantit une précision exacte.
    private BigDecimal montant;

    private StatutIndemnisation statut;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateValidation;

    private Indemnisation(UUID id, UUID dossierId, String numeroDossier, BigDecimal montant,
                           StatutIndemnisation statut, LocalDateTime dateCreation,
                           LocalDateTime dateValidation) {
        this.id = id;
        this.dossierId = dossierId;
        this.numeroDossier = numeroDossier;
        this.montant = montant;
        this.statut = statut;
        this.dateCreation = dateCreation;
        this.dateValidation = dateValidation;
    }

    /**
     * RÈGLE MÉTIER n°1 : création automatique d'une indemnisation en attente.
     *
     * C'est cette méthode qui sera appelée quand indemnisation-service
     * recevra l'événement Kafka "DossierCree" venant de sinistre-service
     * (ce sera l'étape 3, plus tard). Pour l'instant, on la prépare pour
     * qu'elle puisse être appelée manuellement via l'API aussi.
     *
     * Notez : à la création, il n'y a PAS encore de montant (on ne sait pas
     * encore combien indemniser). Le statut est donc forcément EN_ATTENTE.
     */
    public static Indemnisation creerEnAttente(UUID dossierId, String numeroDossier) {
        if (dossierId == null) {
            throw new IllegalArgumentException("L'identifiant du dossier est obligatoire");
        }
        if (numeroDossier == null || numeroDossier.isBlank()) {
            throw new IllegalArgumentException("Le numéro du dossier est obligatoire");
        }
        return new Indemnisation(
                UUID.randomUUID(),
                dossierId,
                numeroDossier,
                null,                          // pas de montant à ce stade
                StatutIndemnisation.EN_ATTENTE,
                LocalDateTime.now(),
                null                            // pas encore validée
        );
    }

    /**
     * RÈGLE MÉTIER n°2 : valider une indemnisation en fixant son montant.
     *
     * On ne peut valider QUE si l'indemnisation est encore EN_ATTENTE.
     * On ne peut pas valider une indemnisation déjà rejetée, ni la
     * revalider si elle l'est déjà. On ne peut pas non plus valider avec
     * un montant négatif ou nul.
     *
     * C'est typiquement le genre de règle qu'on doit écrire ICI, dans le
     * domaine, plutôt que dans le controller ou dans le service Spring -
     * pour être sûr qu'elle s'applique TOUJOURS, peu importe d'où la
     * méthode est appelée (API REST, événement Kafka, tâche planifiée...).
     */
    public void valider(BigDecimal montant) {
        if (this.statut != StatutIndemnisation.EN_ATTENTE) {
            throw new IllegalStateException(
                    "Seule une indemnisation EN_ATTENTE peut être validée (statut actuel : " + this.statut + ")");
        }
        if (montant == null || montant.signum() <= 0) {
            throw new IllegalArgumentException("Le montant doit être strictement positif");
        }
        this.montant = montant;
        this.statut = StatutIndemnisation.VALIDEE;
        this.dateValidation = LocalDateTime.now();
    }

    /**
     * RÈGLE MÉTIER n°3 : rejeter une indemnisation.
     * Même logique : uniquement possible si elle est encore EN_ATTENTE.
     */
    public void rejeter() {
        if (this.statut != StatutIndemnisation.EN_ATTENTE) {
            throw new IllegalStateException(
                    "Seule une indemnisation EN_ATTENTE peut être rejetée (statut actuel : " + this.statut + ")");
        }
        this.statut = StatutIndemnisation.REJETEE;
        this.dateValidation = LocalDateTime.now();
    }

    /**
     * Reconstruction depuis la base de données (utilisée par la couche
     * infrastructure uniquement - jamais par la couche application).
     */
    public static Indemnisation reconstituer(UUID id, UUID dossierId, String numeroDossier, BigDecimal montant,
                                              StatutIndemnisation statut, LocalDateTime dateCreation,
                                              LocalDateTime dateValidation) {
        return new Indemnisation(id, dossierId, numeroDossier, montant, statut, dateCreation, dateValidation);
    }

    public UUID getId() { return id; }
    public UUID getDossierId() { return dossierId; }
    public String getNumeroDossier() { return numeroDossier; }
    public BigDecimal getMontant() { return montant; }
    public StatutIndemnisation getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateValidation() { return dateValidation; }
}
