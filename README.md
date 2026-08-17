# Mini-FGA

Projet d'entraînement simulant une petite architecture microservices pour
la gestion de dossiers de sinistre automobile (inspiré du Fonds de
Garantie Automobile). Objectif : mettre en pratique une architecture
hexagonale, la communication asynchrone entre services via Kafka, et une
authentification JWT partagée entre plusieurs backends.

## Aperçu

| Connexion | Accueil (dossiers) |
|---|---|
| ![Page de connexion](dossier-frontend/docs/captures/01-connexion.png) | ![Accueil, liste des dossiers](dossier-frontend/docs/captures/03-accueil-dossiers.png) |

| Erreur de connexion | Création réussie |
|---|---|
| ![Erreur d'identifiants](dossier-frontend/docs/captures/02-connexion-erreur.png) | ![Bannière de succès](dossier-frontend/docs/captures/04-creation-succes.png) |

## Architecture

```
dossier-frontend (Angular, port 4300)
        │
        ├── POST /api/auth/login  ─────────────►  sinistre-service      (Java/Spring, 8081)
        ├── GET/POST /api/dossiers ───────────►  sinistre-service      (Java/Spring, 8081)
        │
        ├── GET/PUT /api/indemnisations ──────►  indemnisation-service (Java/Spring, 8082)
        │
        └── GET /api/recouvrements ───────────►  recouvrement-service  (Python/FastAPI, 8000)
```

`sinistre-service` est le seul à exposer un endpoint de connexion et à
émettre les tokens JWT ; `indemnisation-service` et `recouvrement-service`
se contentent de les vérifier avec un secret partagé, sans jamais
s'appeler entre eux. Les 3 backends communiquent aussi de façon
asynchrone via **Kafka** (sinistre → indemnisation → recouvrement),
indépendamment du front.

## Les 4 projets

| Dossier | Rôle | Stack |
|---|---|---|
| [`sinistre-service/`](sinistre-service) | Création et consultation des dossiers de sinistre, émission du JWT | Java 17, Spring Boot, Spring Security, PostgreSQL, Kafka |
| [`indemnisation-service/`](indemnisation-service) | Traitement et validation des indemnisations | Java 17, Spring Boot, Spring Security, PostgreSQL, Kafka |
| [`recouvrement-service/`](recouvrement-service) | Suivi du recouvrement après indemnisation | Python 3.12, FastAPI, SQLAlchemy, Kafka |
| [`dossier-frontend/`](dossier-frontend) | Interface Angular (connexion, création, suivi) | Angular 17, TypeScript, RxJS |

Chaque dossier a son propre `README.md` avec le détail de sa structure
interne et les instructions pour le lancer isolément.

## Lancer le projet complet

1. **Infrastructure** (PostgreSQL x2 + Kafka) :
   ```bash
   cd sinistre-service && docker compose up -d
   cd ../indemnisation-service && docker compose up -d
   ```

2. **Les 3 backends**, chacun dans son propre dossier :
   ```bash
   cd sinistre-service        && mvn spring-boot:run   # port 8081
   cd indemnisation-service   && mvn spring-boot:run   # port 8082
   cd recouvrement-service    && uvicorn main:app --port 8000
   ```

3. **Le front-end** :
   ```bash
   cd dossier-frontend
   npm install
   ng serve
   ```
   → http://localhost:4300 — connexion de démo : `admin` / `admin123`.

## Points d'architecture démontrés

- Architecture hexagonale (ports/adapters) sur les 2 services Java
- Communication interservices découplée via événements Kafka plutôt que
  des appels HTTP directs
- Authentification JWT partagée entre 3 services indépendants (un seul
  émet le token, les autres le vérifient sans jamais s'appeler)
- Un même système applicatif en 2 langages (Java/Spring et Python/FastAPI)
  exposant chacun le même contrat de sécurité
