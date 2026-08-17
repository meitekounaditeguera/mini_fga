package com.fga.production.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class BordereauRequest {

    @NotBlank(message = "Le nom de la compagnie d'assurance est obligatoire")
    private String compagnieAssurance;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal montantPrimesCollectees;

    public BordereauRequest() {}

    public String getCompagnieAssurance() { return compagnieAssurance; }
    public void setCompagnieAssurance(String compagnieAssurance) { this.compagnieAssurance = compagnieAssurance; }
    public BigDecimal getMontantPrimesCollectees() { return montantPrimesCollectees; }
    public void setMontantPrimesCollectees(BigDecimal montantPrimesCollectees) { this.montantPrimesCollectees = montantPrimesCollectees; }
}
