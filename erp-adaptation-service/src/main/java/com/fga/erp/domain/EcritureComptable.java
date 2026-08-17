package com.fga.erp.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * L'entité centrale de ce microservice : une "écriture comptable" - le
 * genre de ligne qu'on retrouverait dans un vrai ERP/SAGE. Ce
 * microservice ne fait AUCUN calcul métier complexe (pas de règle comme
 * "5% de contribution") - son seul rôle est d'AGRÉGER fidèlement ce que
 * les autres microservices ont déjà décidé, pour donner une vue
 * financière consolidée. C'est exactement le rôle d'une intégration ERP :
 * ne pas redécider, juste refléter fidèlement.
 */
public class EcritureComptable {

    private final UUID id;
    private final TypeEcriture type;
    private final String reference;
    private final BigDecimal montant;
    private final LocalDateTime dateEcriture;

    private EcritureComptable(UUID id, TypeEcriture type, String reference, BigDecimal montant, LocalDateTime dateEcriture) {
        this.id = id;
        this.type = type;
        this.reference = reference;
        this.montant = montant;
        this.dateEcriture = dateEcriture;
    }

    public static EcritureComptable dossierOuvert(String numeroDossier) {
        return new EcritureComptable(UUID.randomUUID(), TypeEcriture.DOSSIER_OUVERT,
                numeroDossier, BigDecimal.ZERO, LocalDateTime.now());
    }

    public static EcritureComptable indemnisationVersee(String numeroDossier, BigDecimal montant) {
        return new EcritureComptable(UUID.randomUUID(), TypeEcriture.INDEMNISATION_VERSEE,
                numeroDossier, montant, LocalDateTime.now());
    }

    public static EcritureComptable primeRecue(String compagnieAssurance, BigDecimal contributionFga) {
        return new EcritureComptable(UUID.randomUUID(), TypeEcriture.PRIME_RECUE,
                compagnieAssurance, contributionFga, LocalDateTime.now());
    }

    public static EcritureComptable reconstituer(UUID id, TypeEcriture type, String reference,
                                                  BigDecimal montant, LocalDateTime dateEcriture) {
        return new EcritureComptable(id, type, reference, montant, dateEcriture);
    }

    public UUID getId() { return id; }
    public TypeEcriture getType() { return type; }
    public String getReference() { return reference; }
    public BigDecimal getMontant() { return montant; }
    public LocalDateTime getDateEcriture() { return dateEcriture; }
}
