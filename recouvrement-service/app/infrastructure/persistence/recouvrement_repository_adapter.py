"""
L'équivalent Python de IndemnisationRepositoryAdapter.java - la
conversion domaine <-> modèle ORM se fait ici, et nulle part ailleurs.
"""

from typing import Optional
from uuid import UUID

from sqlalchemy.orm import Session

from app.application.port.outbound.recouvrement_repository_port import RecouvrementRepositoryPort
from app.domain.recouvrement import Recouvrement, StatutRecouvrement
from app.infrastructure.persistence.recouvrement_model import RecouvrementModel


class RecouvrementRepositoryAdapter(RecouvrementRepositoryPort):

    def __init__(self, session: Session):
        self._session = session

    def sauvegarder(self, recouvrement: Recouvrement) -> Recouvrement:
        modele = self._vers_modele(recouvrement)
        # merge() : ajoute OU met à jour selon que l'id existe déjà -
        # similaire au comportement de save() côté Spring Data JPA.
        modele_sauvegarde = self._session.merge(modele)
        self._session.commit()
        return self._vers_domaine(modele_sauvegarde)

    def trouver_par_indemnisation_id(self, indemnisation_id: UUID) -> Optional[Recouvrement]:
        modele = self._session.query(RecouvrementModel).filter(
            RecouvrementModel.indemnisation_id == str(indemnisation_id)
        ).first()
        return self._vers_domaine(modele) if modele else None

    def lister_tous(self) -> list[Recouvrement]:
        modeles = self._session.query(RecouvrementModel).all()
        return [self._vers_domaine(m) for m in modeles]

    def _vers_modele(self, recouvrement: Recouvrement) -> RecouvrementModel:
        return RecouvrementModel(
            id=str(recouvrement.id),
            indemnisation_id=str(recouvrement.indemnisation_id),
            dossier_id=str(recouvrement.dossier_id),
            numero_dossier=recouvrement.numero_dossier,
            montant_indemnisation=recouvrement.montant_indemnisation,
            contribution_recouvree=recouvrement.contribution_recouvree,
            statut=recouvrement.statut,
            date_creation=recouvrement.date_creation
        )

    def _vers_domaine(self, modele: RecouvrementModel) -> Recouvrement:
        return Recouvrement(
            id=UUID(modele.id),
            indemnisation_id=UUID(modele.indemnisation_id),
            dossier_id=UUID(modele.dossier_id),
            numero_dossier=modele.numero_dossier,
            montant_indemnisation=modele.montant_indemnisation,
            contribution_recouvree=modele.contribution_recouvree,
            statut=StatutRecouvrement(modele.statut),
            date_creation=modele.date_creation
        )