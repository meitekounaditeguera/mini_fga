package com.fga.production.presentation.dto;

import com.fga.production.domain.BordereauPrime;
import com.fga.production.domain.StatutBordereau;
import java.math.BigDecimal;
import java.util.UUID;

public class BordereauResponse {
    private UUID id;
    private String compagnieAssurance;
    private BigDecimal montantPrimesCollectees;
    private BigDecimal contributionFga;
    private StatutBordereau statut;

    public static BordereauResponse depuis(BordereauPrime bordereau) {
        BordereauResponse r = new BordereauResponse();
        r.id = bordereau.getId();
        r.compagnieAssurance = bordereau.getCompagnieAssurance();
        r.montantPrimesCollectees = bordereau.getMontantPrimesCollectees();
        r.contributionFga = bordereau.getContributionFga();
        r.statut = bordereau.getStatut();
        return r;
    }

    public UUID getId() { return id; }
    public String getCompagnieAssurance() { return compagnieAssurance; }
    public BigDecimal getMontantPrimesCollectees() { return montantPrimesCollectees; }
    public BigDecimal getContributionFga() { return contributionFga; }
    public StatutBordereau getStatut() { return statut; }
}
