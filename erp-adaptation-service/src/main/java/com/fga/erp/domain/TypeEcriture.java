package com.fga.erp.domain;

public enum TypeEcriture {
    DOSSIER_OUVERT,       // traçabilité seule, pas de flux financier
    INDEMNISATION_VERSEE, // sortie d'argent (débit)
    PRIME_RECUE           // entrée d'argent (crédit)
}
