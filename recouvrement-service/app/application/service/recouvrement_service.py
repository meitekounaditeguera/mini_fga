"""
L'équivalent Python de IndemnisationService.java (orchestrateur).
"""

from app.application.port.inbound.traiter_indemnisation_validee_use_case import TraiterIndemnisationValideeUseCase
from app.application.port.outbound.recouvrement_repository_port import RecouvrementRepositoryPort
from app.domain.recouvrement import Recouvrement

from decimal import Decimal
from uuid import UUID
import logging

logger = logging.getLogger(__name__)

class RecouvrementService(TraiterIndemnisationValideeUseCase):
    """
    "class RecouvrementService(TraiterIndemnisationValideeUseCase):" -
    l'équivalent Python de "implements TraiterIndemnisationValideeUseCase"
    en Java. En Python, l'héritage ET l'implémentation d'interface
    utilisent EXACTEMENT la même syntaxe (des parenthèses après le nom
    de la classe) - il n'y a pas de mot-clé "implements" séparé comme
    en Java.
    """

    def __init__(self, repository: RecouvrementRepositoryPort):
        """
        __init__ : l'équivalent du constructeur Java. Remarque qu'on
        reçoit directement l'objet repository en paramètre - pas
        d'injection de dépendances automatique comme avec Spring
        (@Service, @Autowired en coulisses). En Python/FastAPI, on doit
        câbler soi-même "qui reçoit quoi", ce qu'on fera dans main.py
        un peu plus tard.
        """
        self._repository = repository

    def traiter_indemnisation_validee(self, indemnisation_id: UUID, dossier_id: UUID,
                                        numero_dossier: str, montant: Decimal) -> None:
        # IDEMPOTENCE : avant de créer, on vérifie si un recouvrement
        # existe déjà pour cette indemnisation précise. Sans cette
        # vérification, "rejouer" un topic Kafka depuis le début (ce
        # qu'on va faire juste après) créerait des DOUBLONS pour tous
        # les messages déjà traités - un vrai risque en environnement
        # événementiel, où un message peut techniquement être livré
        # plus d'une fois (redémarrage, décalage d'offset, etc.).
        existant = self._repository.trouver_par_indemnisation_id(indemnisation_id)
        if existant is not None:
            logger.info("Recouvrement déjà existant pour l'indemnisation %s, message ignoré (idempotence)",
                        indemnisation_id)
            return
        
        recouvrement = Recouvrement.creer_depuis_indemnisation(
            indemnisation_id, dossier_id, numero_dossier, montant
        )
        self._repository.sauvegarder(recouvrement)