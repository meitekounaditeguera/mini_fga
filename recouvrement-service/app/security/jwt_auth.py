"""
Vérifie le token JWT émis par sinistre-service (POST /api/auth/login) sur
chaque requête entrante. Ce service ne génère jamais de token lui-même -
il partage juste le même secret que sinistre-service et indemnisation-
service pour pouvoir vérifier la signature sans jamais les appeler.

"Depends(verifier_token)" sur une route est l'équivalent, en esprit, du
JwtAuthenticationFilter.java côté Spring - sauf qu'ici, c'est une simple
fonction Python déclarée explicitement sur chaque route à protéger,
plutôt qu'un filtre global appliqué automatiquement à toutes les requêtes.
"""

import jwt
from fastapi import Header, HTTPException

SECRET_PARTAGE = "fga-secret-partage-demo-ne-pas-utiliser-en-production-2026-mini-fga"
ALGORITHME = "HS256"


def verifier_token(authorization: str = Header(default=None)) -> str:
    if authorization is None or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Token manquant")

    token = authorization.removeprefix("Bearer ")

    try:
        payload = jwt.decode(token, SECRET_PARTAGE, algorithms=[ALGORITHME])
    except jwt.PyJWTError:
        raise HTTPException(status_code=401, detail="Token invalide ou expiré")

    return payload["sub"]
