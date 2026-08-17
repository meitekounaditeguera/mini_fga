"""
L'équivalent Python de TraiterDossierCreeUseCase.java (port d'entrée).

ABC (Abstract Base Class) : le mécanisme Python le plus proche d'une
interface Java. Une classe qui hérite d'ABC et qui a des méthodes
marquées @abstractmethod NE PEUT JAMAIS être instanciée directement
(Python lèvera une erreur si tu essaies de faire
TraiterIndemnisationValideeUseCase() directement) - elle sert
uniquement de "contrat" que d'autres classes doivent respecter.

Différence importante avec Java : ce contrat n'est vérifié qu'à
L'EXÉCUTION en Python, jamais à la compilation (puisque Python n'est pas
compilé à l'avance comme Java). Si une classe oublie d'implémenter une
méthode abstraite, l'erreur n'apparaît que quand on essaie de créer une
instance de cette classe - pas avant, contrairement à Java qui refuserait
de compiler.
"""

from abc import ABC, abstractmethod
from decimal import Decimal
from uuid import UUID


class TraiterIndemnisationValideeUseCase(ABC):

    @abstractmethod
    def traiter_indemnisation_validee(self, indemnisation_id: UUID, dossier_id: UUID,
                                        numero_dossier: str, montant: Decimal) -> None:
        """
        Le contrat que le listener Kafka va appeler - exactement le même
        rôle que TraiterDossierCreeUseCase côté indemnisation-service.
        """
        raise NotImplementedError
