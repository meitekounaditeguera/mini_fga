package com.fga.indemnisation.infrastructure.persistence;

import com.fga.indemnisation.domain.StatutIndemnisation;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "indemnisation")
public class IndemnisationEntity {

    @Id
    private UUID id;

    @Column(name = "dossier_id", nullable = false)
    private UUID dossierId;

    @Column(name = "numero_dossier", nullable = false)
    private String numeroDossier;

    // precision/scale : nombre total de chiffres et nombre de décimales,
    // pour que PostgreSQL stocke le montant exactement, sans arrondi -
    // cohérent avec le choix de BigDecimal côté domaine.
    @Column(precision = 15, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutIndemnisation statut;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    protected IndemnisationEntity() {
    }

    public IndemnisationEntity(UUID id, UUID dossierId, String numeroDossier, BigDecimal montant,
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

    public UUID getId() { return id; }
    public UUID getDossierId() { return dossierId; }
    public String getNumeroDossier() { return numeroDossier; }
    public BigDecimal getMontant() { return montant; }
    public StatutIndemnisation getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateValidation() { return dateValidation; }
}