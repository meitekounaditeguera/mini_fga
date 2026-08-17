package com.fga.erp.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Un port d'entrée avec 3 méthodes distinctes - une par type
 * d'événement écouté. Chaque listener Kafka appellera la méthode qui le
 * concerne.
 */
public interface EnregistrerEcritureUseCase {
    void enregistrerOuvertureDossier(UUID dossierId, String numeroDossier);
    void enregistrerIndemnisationVersee(UUID indemnisationId, String numeroDossier, BigDecimal montant);
    void enregistrerPrimeRecue(UUID bordereauId, String compagnieAssurance, BigDecimal contributionFga);
}
