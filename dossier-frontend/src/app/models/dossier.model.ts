/**
 * L'équivalent, côté Angular, de DossierResponse.java côté back-end.
 *
 * TypeScript n'a pas la notion de "record" comme Java, mais une interface
 * joue exactement le même rôle : décrire la FORME des données qu'on
 * s'attend à recevoir du serveur, sans aucun comportement.
 *
 * Remarque : les noms de champs sont EXACTEMENT les mêmes que dans le JSON
 * renvoyé par DossierController (id, numero, nomVictime, etc.) - TypeScript
 * ne fait pas de conversion automatique de nommage, donc il faut que ça
 * corresponde au caractère près.
 */
export interface Dossier {
  id: string;
  numero: string;
  nomVictime: string;
  dateAccident: string;
  lieu: string;
  statut: 'OUVERT' | 'EN_COURS' | 'CLOTURE';
  dateCreation: string;
}

/**
 * L'équivalent de CreerDossierRequest.java : ce qu'on ENVOIE au serveur
 * pour créer un dossier. Remarque qu'il n'y a ni id, ni numero, ni statut -
 * exactement comme côté back-end, c'est le serveur qui les attribue.
 */
export interface CreerDossierRequest {
  nomVictime: string;
  dateAccident: string;
  lieu: string;
}

/**
 * Ce modèle vient d'un AUTRE microservice (indemnisation-service, port 8082),
 * pas de sinistre-service. Remarque qu'il n'a aucun lien de code avec
 * l'interface Dossier ci-dessus - seul le champ dossierId permet de les
 * relier entre eux, une fois les données récupérées séparément.
 */
export interface IndemnisationStatut {
  id: string;
  dossierId: string;
  numeroDossier: string;
  montant: number | null;
  statut: 'EN_ATTENTE' | 'VALIDEE' | 'REJETEE';
}

export interface RecouvrementStatut {
  id: string;
  indemnisation_id: string;
  dossier_id: string;
  numero_dossier: string;
  montant_indemnisation: number;
  contribution_recouvree: number;
  statut: 'EN_ATTENTE' | 'RECOUVRE';
}