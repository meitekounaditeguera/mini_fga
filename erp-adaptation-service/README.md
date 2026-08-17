# erp-adaptation-service

Mini-FGA - Microservice d'agrégation financière, simulant une
intégration ERP/SAGE.

## Rôle
Écoute TROIS topics Kafka différents, provenant de TROIS microservices
différents :
- `dossier-cree` (sinistre-service)
- `indemnisation-validee` (indemnisation-service)
- `bordereau-recu` (production-service)

Il ne prend aucune décision métier - il se contente d'enregistrer
fidèlement une "écriture comptable" par événement reçu, et expose une
synthèse financière consolidée (total primes reçues, total indemnisations
versées, solde).

## Lancer
```bash
docker compose up -d
mvn clean spring-boot:run
```
API sur http://localhost:8084/api/synthese (port 8084, sa propre base sur 5436).

**Important** : ce service doit être lancé APRÈS sinistre-service,
indemnisation-service et production-service, car il ne fait qu'écouter
leurs événements - il ne peut rien produire seul.
