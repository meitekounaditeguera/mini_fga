package com.fga.production.infrastructure.persistence;

import com.fga.production.domain.StatutBordereau;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bordereau_prime")
public class BordereauEntity {

    @Id
    private UUID id;

    @Column(name = "compagnie_assurance", nullable = false)
    private String compagnieAssurance;

    @Column(name = "montant_primes_collectees", precision = 15, scale = 2, nullable = false)
    private BigDecimal montantPrimesCollectees;

    @Column(name = "contribution_fga", precision = 15, scale = 2, nullable = false)
    private BigDecimal contributionFga;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutBordereau statut;

    @Column(name = "date_reception", nullable = false)
    private LocalDateTime dateReception;

    protected BordereauEntity() {}

    public BordereauEntity(UUID id, String compagnieAssurance, BigDecimal montantPrimesCollectees,
                            BigDecimal contributionFga, StatutBordereau statut, LocalDateTime dateReception) {
        this.id = id;
        this.compagnieAssurance = compagnieAssurance;
        this.montantPrimesCollectees = montantPrimesCollectees;
        this.contributionFga = contributionFga;
        this.statut = statut;
        this.dateReception = dateReception;
    }

    public UUID getId() { return id; }
    public String getCompagnieAssurance() { return compagnieAssurance; }
    public BigDecimal getMontantPrimesCollectees() { return montantPrimesCollectees; }
    public BigDecimal getContributionFga() { return contributionFga; }
    public StatutBordereau getStatut() { return statut; }
    public LocalDateTime getDateReception() { return dateReception; }
}
