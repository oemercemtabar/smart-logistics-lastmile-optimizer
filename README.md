# Smart Logistics — Last-Mile Optimizer

A last-mile logistics operations platform:
- Orders + fleet + dispatch workflow (ERP-style)
- Route planning (VRP with constraints, later OR-Tools)
- Live execution tracking via simulated telemetry (GPS + stop events)
- Route plan versioning + audit trail
- Docker-compose: Postgres + Redis + App

## Repo structure
- `backend/` Spring Boot API
- `infra/` docker-compose (Postgres, Redis)
- `simulator/` telemetry simulator (later)
- `docs/` architecture docs

## Quick start (dev)
```bash
docker compose -f infra/docker-compose.yml up -d
```

--- 
## Infrastructure: docker-compose (Postgres + Redis)

Create `infra/docker-compose.yml`:

```yaml
services:
  postgres:
    image: postgres:16
    container_name: slo_postgres
    environment:
      POSTGRES_DB: slo
      POSTGRES_USER: slo
      POSTGRES_PASSWORD: slo
    ports:
      - "5432:5432"
    volumes:
      - slo_pgdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U slo -d slo"]
      interval: 5s
      timeout: 3s
      retries: 20

  redis:
    image: redis:7
    container_name: slo_redis
    ports:
      - "6379:6379"
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 5s
      timeout: 3s
      retries: 20

volumes:
  slo_pgdata:
```
Bring it up:
```bash
docker compose -f infra/docker-compose.yml up -d
docker ps
```