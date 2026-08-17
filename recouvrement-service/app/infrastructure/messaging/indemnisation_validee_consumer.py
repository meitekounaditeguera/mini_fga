"""
L'équivalent Python de DossierCreeEventListener.java - sauf qu'ici, on
doit écrire nous-mêmes la boucle d'écoute, faute d'équivalent à
@KafkaListener dans kafka-python.
"""

import json
import logging
import threading
from decimal import Decimal
from uuid import UUID

from kafka import KafkaConsumer

from app.application.port.inbound.traiter_indemnisation_validee_use_case import TraiterIndemnisationValideeUseCase

# logging.getLogger(__name__) : la convention standard Python - chaque
# fichier crée SON PROPRE logger, nommé d'après son propre module
# (__name__). Ça permet, dans un futur plus avancé, de filtrer ou
# configurer les logs différemment selon leur origine - un peu comme les
# noms de logger basés sur les classes en Java (LoggerFactory.getLogger
# (MaClasse.class)).
logger = logging.getLogger(__name__)


class IndemnisationValideeConsumer:

    def __init__(self, use_case: TraiterIndemnisationValideeUseCase,
                 bootstrap_servers: str = "127.0.0.1:9092",
                 topic: str = "indemnisation-validee",
                 group_id: str = "recouvrement-service"):
        self._use_case = use_case
        self._bootstrap_servers = bootstrap_servers
        self._topic = topic
        self._group_id = group_id
        self._arret_demande = threading.Event()

    def demarrer_en_arriere_plan(self) -> None:
        logger.info("Démarrage du consumer Kafka sur le topic '%s' (thread séparé)...", self._topic)
        thread = threading.Thread(target=self._ecouter, daemon=True)
        thread.start()

    def _ecouter(self) -> None:
        try:
            consumer = KafkaConsumer(
                self._topic,
                bootstrap_servers=self._bootstrap_servers,
                group_id=self._group_id,
                value_deserializer=lambda v: json.loads(v.decode("utf-8")),
                auto_offset_reset="earliest"
            )
            logger.info("Consumer Kafka connecté avec succès à %s (topic='%s', group_id='%s')",
                        self._bootstrap_servers, self._topic, self._group_id)
        except Exception:
            logger.error("Impossible de se connecter à Kafka sur %s", self._bootstrap_servers, exc_info=True)
            return

        logger.info("En attente de messages sur '%s'...", self._topic)

        for message in consumer:
            if self._arret_demande.is_set():
                break

            logger.info("Message reçu sur '%s' : %s", self._topic, message.value)

            try:
                self._traiter_message(message.value)
                logger.info("Recouvrement créé avec succès pour l'indemnisation %s",
                            message.value.get("indemnisationId"))
            except Exception:
                logger.error("Erreur lors du traitement du message : %s", message.value, exc_info=True)

    def _traiter_message(self, evenement: dict) -> None:
        self._use_case.traiter_indemnisation_validee(
            indemnisation_id=UUID(evenement["indemnisationId"]),
            dossier_id=UUID(evenement["dossierId"]),
            numero_dossier=evenement["numeroDossier"],
            montant=Decimal(str(evenement["montant"]))
        )