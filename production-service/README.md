# production-service

Mini-FGA - Microservice de gestion des bordereaux de primes d'assurance.

## Rôle
Reçoit les bordereaux de primes envoyés par les compagnies d'assurance,
calcule la contribution FGA (2% du montant collecté), et publie un
événement Kafka `bordereau-recu` pour informer les autres services
(notamment erp-adaptation-service).

## Lancer
```bash
docker compose up -d
mvn clean spring-boot:run
```
API sur http://localhost:8083/api/bordereaux (port 8083, sa propre base sur 5435).

## Exemple de requête
```http
POST http://localhost:8083/api/bordereaux
Content-Type: application/json

{
  "compagnieAssurance": "NSIA Assurances",
  "montantPrimesCollectees": 5000000
}
```
