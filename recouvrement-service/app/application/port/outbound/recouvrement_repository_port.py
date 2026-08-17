"""
L'équivalent Python de IndemnisationRepositoryPort.java (port de sortie).
"""

from abc import ABC, abstractmethod
from typing import Optional
from uuid import UUID

from app.domain.recouvrement import Recouvrement


class RecouvrementRepositoryPort(ABC):

    @abstractmethod
    def sauvegarder(self, recouvrement: Recouvrement) -> Recouvrement:
        raise NotImplementedError

    @abstractmethod
    def trouver_par_indemnisation_id(self, indemnisation_id: UUID) -> Optional[Recouvrement]:
        """
        Optional[Recouvrement] : l'équivalent Python de Optional<Indemnisation>
        en Java - indique explicitement que cette méthode peut renvoyer
        soit un Recouvrement, soit None (l'équivalent Python de null).
        """
        raise NotImplementedError

    @abstractmethod
    def lister_tous(self) -> list[Recouvrement]:
        raise NotImplementedError
