"""
L'équivalent Python de IndemnisationEntity.java - la version "base de
données" du domaine, avec les détails techniques de stockage.
"""

from sqlalchemy import Column, String, Numeric, DateTime, Enum as SQLEnum
from sqlalchemy.dialects.postgresql import UUID as PG_UUID
import uuid

from app.domain.recouvrement import StatutRecouvrement
from app.infrastructure.persistence.database import Base


class RecouvrementModel(Base):
    """
    "__tablename__" : équivalent de @Table(name = "...") en Java.
    Chaque "Column(...)" est l'équivalent d'un champ annoté @Column.

    Remarque : contrairement à Java où DossierEntity et Dossier étaient
    deux classes séparées avec une conversion manuelle entre les deux
    (versEntity/versDomaine), certains développeurs Python choisissent
    de FUSIONNER domaine et modèle ORM en une seule classe, pour aller
    plus vite. On ne le fait volontairement PAS ici : on garde la
    séparation stricte domaine/infrastructure, comme en Java, parce que
    c'est le principe qu'on veut ancrer, peu importe le langage.
    """
    __tablename__ = "recouvrement"

    # SQLite ne stocke pas nativement un vrai type UUID (contrairement à
    # PostgreSQL) - on le stocke ici comme une simple chaîne de
    # caractères (String(36), la longueur exacte d'un UUID écrit en texte).
    id = Column(String(36), primary_key=True)
    indemnisation_id = Column(String(36), nullable=False)
    dossier_id = Column(String(36), nullable=False)
    numero_dossier = Column(String, nullable=False)
    montant_indemnisation = Column(Numeric(15, 2), nullable=False)
    contribution_recouvree = Column(Numeric(15, 2), nullable=False)
    statut = Column(SQLEnum(StatutRecouvrement), nullable=False)
    date_creation = Column(DateTime, nullable=False)