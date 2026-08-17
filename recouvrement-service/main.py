"""
L'équivalent de IndemnisationServiceApplication.java, mais avec une
différence de taille : Spring Boot assemble automatiquement toutes les
pièces (grâce aux annotations @Service, @Component, @Repository...).
Ici, PERSONNE ne le fait à notre place : ce fichier doit explicitement
créer chaque objet et les brancher les uns aux autres, dans le bon ordre.
C'est ce qu'on appelle parfois le "composition root" - l'endroit unique
où toute l'application est câblée.
"""

from contextlib import asynccontextmanager

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.application.service.recouvrement_service import RecouvrementService
from app.infrastructure.messaging.indemnisation_validee_consumer import IndemnisationValideeConsumer
from app.infrastructure.persistence.database import Base, engine, SessionLocal
from app.infrastructure.persistence.recouvrement_repository_adapter import RecouvrementRepositoryAdapter
from app.presentation.recouvrement_controller import router

import logging

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s"
)


@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    "lifespan" : l'équivalent conceptuel de ce qui se passe
    automatiquement au démarrage/arrêt d'un Spring Boot (créer les
    tables, etc.), mais qu'on doit ici déclencher nous-mêmes,
    explicitement, à un moment précis du cycle de vie de l'application.

    Tout ce qui est AVANT le "yield" s'exécute au DÉMARRAGE de
    l'application (une seule fois). Tout ce qui serait après le "yield"
    s'exécuterait à l'ARRÊT (on n'en a pas besoin ici).
    """
    # L'équivalent de "ddl-auto: update" côté Java : demande à
    # SQLAlchemy de créer les tables si elles n'existent pas déjà.
    Base.metadata.create_all(bind=engine)

    # Assemblage manuel des pièces (le "composition root" dont on parlait) :
    session = SessionLocal()
    repository = RecouvrementRepositoryAdapter(session)
    service = RecouvrementService(repository)
    consumer = IndemnisationValideeConsumer(use_case=service)
    consumer.demarrer_en_arriere_plan()

    yield  # L'application tourne ici, entre le démarrage et l'arrêt.


app = FastAPI(
    title="recouvrement-service",
    description="Mini-FGA - Microservice de recouvrement (écrit en Python/FastAPI)",
    lifespan=lifespan
)

# "include_router" : on branche les routes définies dans
# recouvrement_controller.py sur l'application principale - un peu comme
# si on disait "toutes les routes de ce fichier font partie de mon appli".
app.include_router(router)

# CORSMiddleware : l'équivalent FastAPI de WebConfig.java côté Spring.
# Syntaxe différente (un "middleware" à ajouter, plutôt qu'une classe
# @Configuration dédiée), mais exactement le même but : autoriser le
# navigateur à accepter les réponses de ce serveur quand elles sont
# demandées depuis http://localhost:4300 (Angular).
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4300"],
    allow_methods=["GET", "POST", "PUT", "DELETE"],
    allow_headers=["*"],
)