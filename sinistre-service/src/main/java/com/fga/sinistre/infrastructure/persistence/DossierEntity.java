package com.fga.sinistre.infrastructure.persistence;

import com.fga.sinistre.domain.StatutDossier;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * L'entité JPA. Elle ne vit QUE dans la couche infrastructure.
 * C'est la traduction technique du domaine pour PostgreSQL — le domaine
 * (Dossier.java) n'a jamais connaissance de cette classe.
 */
@Entity
@Table(name = "dossier")
public class DossierEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(name = "nom_victime", nullable = false)
    private String nomVictime;

    @Column(name = "date_accident", nullable = false)
    private LocalDate dateAccident;

    private String lieu;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDossier statut;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    protected DossierEntity() {
        // constructeur requis par JPA
    }

    public DossierEntity(UUID id, String numero, String nomVictime, LocalDate dateAccident,
                          String lieu, StatutDossier statut, LocalDateTime dateCreation) {
        this.id = id;
        this.numero = numero;
        this.nomVictime = nomVictime;
        this.dateAccident = dateAccident;
        this.lieu = lieu;
        this.statut = statut;
        this.dateCreation = dateCreation;
    }

    public UUID getId() { return id; }
    public String getNumero() { return numero; }
    public String getNomVictime() { return nomVictime; }
    public LocalDate getDateAccident() { return dateAccident; }
    public String getLieu() { return lieu; }
    public StatutDossier getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
}
