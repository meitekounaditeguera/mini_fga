"""
Pydantic : contrairement à IndemnisationResponse.java (une classe "à la
main" avec des getters), Pydantic génère automatiquement :
- la conversion vers/depuis JSON
- la VALIDATION des types (l'équivalent de Bean Validation @NotNull...)
- la documentation automatique de l'API (FastAPI l'utilise pour générer
  une doc interactive, visible sur /docs une fois l'appli lancée - un
  vrai plus par rapport à Swagger qu'il faut configurer en plus côté Java)
"""

from decimal import Decimal
from uuid import UUID

from pydantic import BaseModel

from app.domain.recouvrement import Recouvrement, StatutRecouvrement


class RecouvrementResponse(BaseModel):
    """
    BaseModel : la classe dont hérite CHAQUE schéma Pydantic - c'est elle
    qui apporte toute la magie de validation/sérialisation. Remarque
    qu'on déclare juste les champs et leur type, sans écrire aucun
    getter/setter - Pydantic les génère pour toi.
    """
    id: UUID
    indemnisation_id: UUID
    dossier_id: UUID
    numero_dossier: str
    montant_indemnisation: Decimal
    contribution_recouvree: Decimal
    statut: StatutRecouvrement

    @staticmethod
    def depuis(recouvrement: Recouvrement) -> "RecouvrementResponse":
        return RecouvrementResponse(
            id=recouvrement.id,
            indemnisation_id=recouvrement.indemnisation_id,
            dossier_id=recouvrement.dossier_id,
            numero_dossier=recouvrement.numero_dossier,
            montant_indemnisation=recouvrement.montant_indemnisation,
            contribution_recouvree=recouvrement.contribution_recouvree,
            statut=recouvrement.statut
        )