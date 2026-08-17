package com.fga.erp.presentation.dto;

import com.fga.erp.domain.EcritureComptable;
import com.fga.erp.domain.TypeEcriture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class EcritureResponse {
    private UUID id;
    private TypeEcriture type;
    private String reference;
    private BigDecimal montant;
    private LocalDateTime dateEcriture;

    public static EcritureResponse depuis(EcritureComptable e) {
        EcritureResponse r = new EcritureResponse();
        r.id = e.getId();
        r.type = e.getType();
        r.reference = e.getReference();
        r.montant = e.getMontant();
        r.dateEcriture = e.getDateEcriture();
        return r;
    }

    public UUID getId() { return id; }
    public TypeEcriture getType() { return type; }
    public String getReference() { return reference; }
    public BigDecimal getMontant() { return montant; }
    public LocalDateTime getDateEcriture() { return dateEcriture; }
}
