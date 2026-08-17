package com.fga.erp.infrastructure.persistence;

import com.fga.erp.domain.TypeEcriture;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ecriture_comptable")
public class EcritureEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeEcriture type;

    @Column(nullable = false)
    private String reference;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal montant;

    @Column(name = "date_ecriture", nullable = false)
    private LocalDateTime dateEcriture;

    protected EcritureEntity() {}

    public EcritureEntity(UUID id, TypeEcriture type, String reference, BigDecimal montant, LocalDateTime dateEcriture) {
        this.id = id;
        this.type = type;
        this.reference = reference;
        this.montant = montant;
        this.dateEcriture = dateEcriture;
    }

    public UUID getId() { return id; }
    public TypeEcriture getType() { return type; }
    public String getReference() { return reference; }
    public BigDecimal getMontant() { return montant; }
    public LocalDateTime getDateEcriture() { return dateEcriture; }
}
