package com.fga.sinistre.application.port.in;

import com.fga.sinistre.domain.Dossier;

import java.time.LocalDate;

/**
 * Port d'ENTRÉE : c'est le contrat que le controller (couche presentation)
 * va appeler. Le controller ne connaît que cette interface, jamais
 * l'implémentation concrète.
 */
public interface CreerDossierUseCase {

    Dossier creerDossier(String nomVictime, LocalDate dateAccident, String lieu);
}
