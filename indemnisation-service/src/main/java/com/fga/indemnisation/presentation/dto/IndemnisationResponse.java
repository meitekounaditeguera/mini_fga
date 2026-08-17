package com.fga.indemnisation.presentation.dto;

import com.fga.indemnisation.domain.Indemnisation;
import com.fga.indemnisation.domain.StatutIndemnisation;

import java.math.BigDecimal;
import java.util.UUID;

public class IndemnisationResponse {

    private UUID id;
    private UUID dossierId;
    private String numeroDossier;
    private BigDecimal montant;
    private StatutIndemnisation statut;

    public static IndemnisationResponse depuis(Indemnisation indemnisation) {
        IndemnisationResponse response = new IndemnisationResponse();
        response.id = indemnisation.getId();
        response.dossierId = indemnisation.getDossierId();
        response.numeroDossier = indemnisation.getNumeroDossier();
        response.montant = indemnisation.getMontant();
        response.statut = indemnisation.getStatut();
        return response;
    }

    public UUID getId() { return id; }
    public UUID getDossierId() { return dossierId; }
    public String getNumeroDossier() { return numeroDossier; }
    public BigDecimal getMontant() { return montant; }
    public StatutIndemnisation getStatut() { return statut; }
}