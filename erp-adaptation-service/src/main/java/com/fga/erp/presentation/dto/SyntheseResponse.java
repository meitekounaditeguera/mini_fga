package com.fga.erp.presentation.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Regroupe la liste des écritures ET un solde global calculé
 * (primes reçues - indemnisations versées) - une vraie petite "synthèse
 * financière", comme le ferait un tableau de bord ERP.
 */
public class SyntheseResponse {
    private List<EcritureResponse> ecritures;
    private BigDecimal totalPrimesRecues;
    private BigDecimal totalIndemnisationsVersees;
    private BigDecimal solde;

    public SyntheseResponse(List<EcritureResponse> ecritures, BigDecimal totalPrimesRecues,
                             BigDecimal totalIndemnisationsVersees, BigDecimal solde) {
        this.ecritures = ecritures;
        this.totalPrimesRecues = totalPrimesRecues;
        this.totalIndemnisationsVersees = totalIndemnisationsVersees;
        this.solde = solde;
    }

    public List<EcritureResponse> getEcritures() { return ecritures; }
    public BigDecimal getTotalPrimesRecues() { return totalPrimesRecues; }
    public BigDecimal getTotalIndemnisationsVersees() { return totalIndemnisationsVersees; }
    public BigDecimal getSolde() { return solde; }
}
