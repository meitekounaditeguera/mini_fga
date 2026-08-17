# sinistre-service — Mini-FGA (projet d'entraînement)

Premier microservice du projet d'entraînement "Mini-FGA", en architecture
hexagonale (le même style que le vrai projet FGA).

## Structure du projet (les 4 couches)

```
com.fga.sinistre
├── presentation/        → Controllers REST + DTOs (le port d'entrée HTTP)
├── application/
│   ├── port/in/          → Interfaces des use cases (ce que l'API peut demander)
│   ├── port/out/         → Interfaces vers l'extérieur (ex: persistance)
│   └── service/          → Implémentation des use cases (l'orchestrateur)
├── domain/               → Règles métier pures (aucune dépendance technique)
└── infrastructure/
    └── persistence/      → JPA, entités techniques, adaptateur PostgreSQL
```

Le principe clé : les flèches de dépendance vont TOUJOURS vers le domaine.
Le domaine ne connaît ni Spring, ni JPA, ni PostgreSQL.

## Lancer le projet

1. Démarrer PostgreSQL :
   ```bash
   docker compose up -d
   ```

2. Lancer l'application :
   ```bash
   ./mvnw spring-boot:run
   ```

3. Tester l'API :
   ```bash
   # Créer un dossier
   curl -X POST http://localhost:8081/api/dossiers \
     -H "Content-Type: application/json" \
     -d '{"nomVictime": "Kouassi Jean", "dateAccident": "2026-07-01", "lieu": "Abidjan - Cocody"}'

   # Lister les dossiers
   curl http://localhost:8081/api/dossiers
   ```

## Authentification

`POST /api/auth/login` (public) accepte `{"utilisateur", "motDePasse"}` et
renvoie un JWT (HS256) si les identifiants correspondent au compte de
démonstration (`admin` / `admin123` - aucune base utilisateurs pour
l'instant). Toutes les autres routes (`/api/dossiers/**`) exigent ce token
dans l'en-tête `Authorization: Bearer <token>`.

Le secret de signature (`fga.jwt.secret` dans `application.yml`) est
partagé tel quel avec `indemnisation-service` et `recouvrement-service`,
qui vérifient le même token sans jamais appeler ce service.

## Ce que ce microservice ne fait pas encore (prochains sprints)

- Sprint 2 : publier un événement Kafka `DossierCree` quand un dossier est créé,
  pour que `indemnisation-service` puisse réagir automatiquement.
- Sprint 3 : petit frontend Angular pour créer/lister les dossiers.
- Une vraie base utilisateurs (au lieu du compte de démo codé en dur) et,
  éventuellement, Keycloak à la place du JWT maison si le projet grandit.
- Optionnel : Camunda pour orchestrer le workflow du dossier.
