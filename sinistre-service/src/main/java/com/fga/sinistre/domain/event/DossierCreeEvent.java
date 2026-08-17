package com.fga.sinistre.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * L'ÉVÉNEMENT : c'est ce message précis qui sera écrit sur le "panneau
 * d'affichage" Kafka (le topic "dossier-cree"), quand un nouveau dossier
 * est ouvert.
 *
 * Règle importante : un événement contient UNIQUEMENT les informations
 * dont les autres microservices ont besoin - pas l'objet Dossier complet.
 * indemnisation-service n'a pas besoin de connaître le "lieu" de l'accident,
 * par exemple, donc on ne l'inclut pas.
 *
 * C'est un "record" Java (depuis Java 16) : une façon compacte d'écrire
 * une classe immuable qui ne fait QUE porter des données, sans aucun
 * comportement. Parfait pour un événement, qui est un simple fait
 * "il s'est passé ceci, à cet instant" - un événement ne change jamais
 * une fois créé.
 */
public record DossierCreeEvent(
        UUID dossierId,
        String numeroDossier,
        String nomVictime,
        LocalDateTime dateEvenement
) {

    /**
     * Petite méthode fabrique, comme on l'a fait pour Dossier.ouvrir(...) :
     * ça évite de manipuler le constructeur du record directement partout
     * dans le code, et ça centralise le "dateEvenement = maintenant".
     */
    public static DossierCreeEvent depuis(UUID dossierId, String numeroDossier, String nomVictime) {
        return new DossierCreeEvent(dossierId, numeroDossier, nomVictime, LocalDateTime.now());
    }
}