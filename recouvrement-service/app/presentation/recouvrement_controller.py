"""
FastAPI n'a pas de "@RestController" comme Spring. À la place, on utilise
un "APIRouter" - un objet auquel on rattache des routes, qu'on "branche"
ensuite sur l'application principale dans main.py.
"""

from uuid import UUID

from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session

from app.application.port.outbound.recouvrement_repository_port import RecouvrementRepositoryPort
from app.infrastructure.persistence.database import SessionLocal
from app.infrastructure.persistence.recouvrement_repository_adapter import RecouvrementRepositoryAdapter
from app.presentation.recouvrement_response import RecouvrementResponse
from app.security.jwt_auth import verifier_token

router = APIRouter(prefix="/api/recouvrements")


def obtenir_session():
    """
    Une "dependency" FastAPI (le mécanisme derrière Depends(...) plus
    bas). Elle ouvre une session de base de données au début d'une
    requête, et la ferme automatiquement à la fin (grâce au "yield"
    plutôt que "return" - ça met la fonction en pause après avoir donné
    la session, puis reprend son exécution APRÈS que la requête soit
    terminée, pour fermer proprement). C'est l'équivalent, en esprit, de
    ce que Spring fait automatiquement avec ses transactions - sauf
    qu'ici, c'est explicite et visible dans le code.
    """
    session = SessionLocal()
    try:
        yield session
    finally:
        session.close()


def obtenir_repository(session: Session = Depends(obtenir_session)) -> RecouvrementRepositoryPort:
    """
    "Depends(obtenir_session)" : dit à FastAPI "avant d'appeler cette
    fonction, appelle d'abord obtenir_session() et donne-moi son
    résultat". C'est LE mécanisme d'injection de dépendances de FastAPI -
    beaucoup plus explicite et manuel que l'injection automatique de
    Spring (@Autowired en coulisses), mais ça reste le même principe :
    "je déclare ce dont j'ai besoin, quelque chose me le fournit".
    """
    return RecouvrementRepositoryAdapter(session)


@router.get("/{indemnisation_id}", response_model=RecouvrementResponse)
def consulter_par_indemnisation(indemnisation_id: UUID,
                                  repository: RecouvrementRepositoryPort = Depends(obtenir_repository),
                                  utilisateur: str = Depends(verifier_token)):
    """
    "@router.get(...)" : l'équivalent de @GetMapping côté Java.
    "response_model=RecouvrementResponse" : dit à FastAPI de valider et
    documenter automatiquement la forme de la réponse.
    """
    recouvrement = repository.trouver_par_indemnisation_id(indemnisation_id)
    if recouvrement is None:
        # HTTPException : l'équivalent Python de ce que
        # GlobalExceptionHandler.java faisait pour NoSuchElementException -
        # sauf qu'ici, pas besoin de classe séparée, FastAPI gère
        # nativement la traduction en réponse HTTP.
        raise HTTPException(status_code=404, detail=f"Aucun recouvrement trouvé pour l'indemnisation {indemnisation_id}")
    return RecouvrementResponse.depuis(recouvrement)