package com.fga.sinistre.domain;

/**
 * Les statuts possibles d'un dossier de sinistre.
 * C'est une règle métier : elle ne dépend d'aucune techno (pas de JPA ici).
 */
public enum StatutDossier {
    OUVERT,
    EN_COURS,
    CLOTURE
}
