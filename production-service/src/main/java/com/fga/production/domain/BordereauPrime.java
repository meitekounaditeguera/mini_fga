package com.fga.production.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Le domaine de production-service : un bordereau de prime, envoyé
 * périodiquement par une compagnie d'assurance. D'après les documents
 * FGA, 2% des primes RC Auto collectées sont reversés au FGA - c'est
 * cette contribution qu'on calcule ici, symétriquement à la contribution
 * de recouvrement (5%) calculée côté Python.
 */
public class BordereauPrime {

    public static final BigDecimal TAUX_CONTRIBUTION_FGA = new BigDecimal("0.02"); // 2%

    private final UUID id;
    private final String compagnieAssurance;
    private final BigDecimal montantPrimesCollectees;
    private final BigDecimal contributionFga;
    private StatutBordereau statut;
    private final LocalDateTime dateReception;

    private BordereauPrime(UUID id, String compagnieAssurance, BigDecimal montantPrimesCollectees,
                            BigDecimal contributionFga, StatutBordereau statut, LocalDateTime dateReception) {
        this.id = id;
        this.compagnieAssurance = compagnieAssurance;
        this.montantPrimesCollectees = montantPrimesCollectees;
        this.contributionFga = contributionFga;
        this.statut = statut;
        this.dateReception = dateReception;
    }

    public static BordereauPrime recevoir(String compagnieAssurance, BigDecimal montantPrimesCollectees) {
        if (compagnieAssurance == null || compagnieAssurance.isBlank()) {
            throw new IllegalArgumentException("Le nom de la compagnie d'assurance est obligatoire");
        }
        if (montantPrimesCollectees == null || montantPrimesCollectees.signum() <= 0) {
            throw new IllegalArgumentException("Le montant des primes collectées doit être positif");
        }
        BigDecimal contribution = montantPrimesCollectees.multiply(TAUX_CONTRIBUTION_FGA)
                .setScale(2, java.math.RoundingMode.HALF_UP);
        return new BordereauPrime(UUID.randomUUID(), compagnieAssurance, montantPrimesCollectees,
                contribution, StatutBordereau.RECU, LocalDateTime.now());
    }

    public static BordereauPrime reconstituer(UUID id, String compagnieAssurance, BigDecimal montantPrimesCollectees,
                                               BigDecimal contributionFga, StatutBordereau statut,
                                               LocalDateTime dateReception) {
        return new BordereauPrime(id, compagnieAssurance, montantPrimesCollectees, contributionFga, statut, dateReception);
    }

    public UUID getId() { return id; }
    public String getCompagnieAssurance() { return compagnieAssurance; }
    public BigDecimal getMontantPrimesCollectees() { return montantPrimesCollectees; }
    public BigDecimal getContributionFga() { return contributionFga; }
    public StatutBordereau getStatut() { return statut; }
    public LocalDateTime getDateReception() { return dateReception; }
}
