"""
SQLAlchemy : l'ORM Python (équivalent d'Hibernate/JPA côté Java).
Ce fichier configure la connexion à la base - l'équivalent de la section
"datasource" dans application.yml, mais écrit en code Python plutôt
qu'en configuration YAML.
"""

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base

# "sqlite:///./recouvrement.db" : le format d'URL de connexion SQLite.
# Remarque bien qu'il n'y a NI host, NI port, NI username/password
# (contrairement à jdbc:postgresql://localhost:5434/...) - juste un
# chemin vers un fichier sur le disque, créé automatiquement s'il
# n'existe pas encore.
DATABASE_URL = "sqlite:///./recouvrement.db"

# "engine" : la connexion technique à la base - l'équivalent du
# DataSource configuré automatiquement par Spring Boot à partir
# d'application.yml.
engine = create_engine(
    DATABASE_URL,
    # SQLite, par défaut, refuse qu'un objet connexion soit utilisé
    # depuis un thread différent de celui qui l'a créé - une protection
    # trop stricte pour notre usage (FastAPI + un thread séparé pour
    # Kafka, qu'on verra dans l'étape suivante). On la désactive ici.
    connect_args={"check_same_thread": False}
)

# SessionLocal : une "fabrique" de sessions de base de données.
# Une session, c'est une conversation avec la base (ouvrir, lire/écrire,
# fermer) - l'équivalent conceptuel d'une transaction Hibernate.
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Base : la classe dont TOUS nos modèles ORM (l'équivalent des classes
# @Entity Java) devront hériter, pour que SQLAlchemy sache les
# reconnaître et créer les tables correspondantes.
Base = declarative_base()