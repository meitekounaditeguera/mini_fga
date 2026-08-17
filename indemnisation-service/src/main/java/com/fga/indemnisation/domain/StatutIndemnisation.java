package com.fga.indemnisation.domain;

/**
 * Les statuts possibles d'une indemnisation.
 *
 * EN_ATTENTE   : l'indemnisation vient d'être créée automatiquement,
 *                suite à l'ouverture d'un dossier de sinistre. On ne connaît
 *                pas encore le montant.
 * VALIDEE      : un gestionnaire a fixé un montant et validé l'indemnisation.
 * REJETEE      : l'indemnisation a été refusée (ex: dossier non recevable).
 */
public enum StatutIndemnisation {
    EN_ATTENTE,
    VALIDEE,
    REJETEE
}
