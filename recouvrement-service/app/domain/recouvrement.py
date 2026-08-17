"""
L'équivalent Python de Indemnisation.java (domaine pur).

Différences de syntaxe à retenir par rapport à Java :
- Pas de mot-clé "class Foo { private String x; }" avec accolades -
  Python utilise l'indentation (les espaces) pour délimiter les blocs de
  code, PAS de point-virgule en fin de ligne, PAS d'accolades.
- "self" (au lieu de "this" en Java) : le premier paramètre de CHAQUE
  méthode d'instance, représentant explicitement l'objet lui-même.
  Java le fait implicitement ("this" n'a pas besoin d'être déclaré),
  Python te force à l'écrire explicitement partout.
- Pas de "private"/"public" : Python n'a pas de vraie visibilité stricte
  comme Java. Par convention, un underscore devant un nom (_montant)
  signifie "usage interne, ne pas toucher depuis l'extérieur" - mais
  RIEN n'empêche techniquement d'y accéder. C'est une convention de
  confiance entre développeurs, pas une barrière du langage.
"""

from dataclasses import dataclass, field
from datetime import datetime
from decimal import Decimal
from enum import Enum
from uuid import UUID, uuid4


class StatutRecouvrement(str, Enum):
    """
    L'équivalent de StatutIndemnisation.java (enum).
    "str, Enum" : fait en sorte que ce statut se comporte aussi comme une
    chaîne de caractères (utile pour la sérialisation JSON automatique).
    """
    EN_ATTENTE = "EN_ATTENTE"
    RECOUVRE = "RECOUVRE"


@dataclass
class Recouvrement:
    """
    @dataclass : un "décorateur" Python (reconnaissable au @ devant) qui
    génère automatiquement le constructeur, à partir des champs déclarés
    juste en dessous. C'est un peu l'équivalent du "record" Java qu'on
    avait utilisé pour DossierCreeEvent - sauf qu'ici, contrairement à un
    record Java qui est immuable, une dataclass reste modifiable par
    défaut (on pourrait changer ça avec frozen=True, mais on ne le fait
    pas ici car on doit modifier le statut après création).
    """
    id: UUID
    indemnisation_id: UUID
    dossier_id: UUID
    numero_dossier: str
    montant_indemnisation: Decimal
    contribution_recouvree: Decimal
    statut: StatutRecouvrement
    date_creation: datetime

    # Le pourcentage de contribution que le FGA recouvre sur chaque
    # indemnisation versée - une règle métier simple, en dur ici pour
    # l'exercice (dans un vrai projet, ça viendrait probablement d'une
    # configuration ou d'une table de paramètres).
    TAUX_CONTRIBUTION = Decimal("0.05")  # 5%

    @staticmethod
    def creer_depuis_indemnisation(indemnisation_id: UUID, dossier_id: UUID,
                                     numero_dossier: str, montant_indemnisation: Decimal) -> "Recouvrement":
        """
        L'équivalent de Indemnisation.creerEnAttente(...) côté Java :
        une "factory method" - ici, un @staticmethod (pas besoin d'une
        instance existante pour l'appeler, comme "static" en Java).

        Remarque le type de retour '"Recouvrement"' entre guillemets :
        en Python, une classe ne peut pas se référencer elle-même
        directement dans ses propres annotations de type avant d'être
        entièrement définie - les guillemets contournent ce problème
        (on appelle ça un "forward reference").
        """
        if montant_indemnisation <= 0:
            raise ValueError("Le montant de l'indemnisation doit être positif")

        contribution = (montant_indemnisation * Recouvrement.TAUX_CONTRIBUTION).quantize(Decimal("0.01"))

        return Recouvrement(
            id=uuid4(),
            indemnisation_id=indemnisation_id,
            dossier_id=dossier_id,
            numero_dossier=numero_dossier,
            montant_indemnisation=montant_indemnisation,
            contribution_recouvree=contribution,
            statut=StatutRecouvrement.EN_ATTENTE,
            date_creation=datetime.now()
        )