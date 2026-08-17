package com.fga.sinistre.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Le dossier de sinistre, tel que défini par le métier.
 *
 * Point important : cette classe ne contient AUCUNE annotation technique
 * (pas de @Entity, pas de @Column). C'est le principe de l'architecture
 * hexagonale : le domaine ne sait pas comment il sera stocké.
 * C'est la couche infrastructure qui s'occupera de le persister.
 */
public class Dossier {

    private final UUID id;
    private final String numero;
    private final String nomVictime;
    private final LocalDate dateAccident;
    private final String lieu;
    private StatutDossier statut;
    private final LocalDateTime dateCreation;

    private Dossier(UUID id, String numero, String nomVictime, LocalDate dateAccident,
                     String lieu, StatutDossier statut, LocalDateTime dateCreation) {
        this.id = id;
        this.numero = numero;
        this.nomVictime = nomVictime;
        this.dateAccident = dateAccident;
        this.lieu = lieu;
        this.statut = statut;
        this.dateCreation = dateCreation;
    }

    /**
     * Règle métier : ouvrir un nouveau dossier.
     * Un dossier nouvellement ouvert est toujours au statut OUVERT.
     */
    public static Dossier ouvrir(String numero, String nomVictime, LocalDate dateAccident, String lieu) {
        if (nomVictime == null || nomVictime.isBlank()) {
            throw new IllegalArgumentException("Le nom de la victime est obligatoire");
        }
        if (dateAccident == null || dateAccident.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de l'accident doit être renseignée et ne peut pas être dans le futur");
        }
        return new Dossier(UUID.randomUUID(), numero, nomVictime, dateAccident, lieu,
                StatutDossier.OUVERT, LocalDateTime.now());
    }

    /**
     * Reconstruction d'un dossier existant (utilisé par la couche infrastructure
     * quand elle relit un dossier depuis la base de données).
     */
    public static Dossier reconstituer(UUID id, String numero, String nomVictime, LocalDate dateAccident,
                                        String lieu, StatutDossier statut, LocalDateTime dateCreation) {
        return new Dossier(id, numero, nomVictime, dateAccident, lieu, statut, dateCreation);
    }

    public void passerEnCours() {
        if (this.statut == StatutDossier.CLOTURE) {
            throw new IllegalStateException("Un dossier clôturé ne peut pas repasser en cours");
        }
        this.statut = StatutDossier.EN_COURS;
    }

    public UUID getId() { return id; }
    public String getNumero() { return numero; }
    public String getNomVictime() { return nomVictime; }
    public LocalDate getDateAccident() { return dateAccident; }
    public String getLieu() { return lieu; }
    public StatutDossier getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
}
