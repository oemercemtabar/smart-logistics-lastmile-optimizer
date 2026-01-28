# Smart Logistics — Last-Mile Optimizer

A last-mile logistics operations platform (ERP-style) focused on **fleet + dispatch + route planning**.

**Core goals**
- Orders / delivery tasks, fleet, dispatch workflow
- Route planning (VRP constraints; later OR-Tools integration)
- Execution tracking via simulated telemetry (GPS + stop events — planned)
- Route plan versioning + audit trail (planned)
- Local dev stack via Docker Compose: **Postgres + Redis + App**

---

## Tech stack
- **Backend:** Spring Boot (Gradle), Java 21
- **Database:** PostgreSQL
- **Cache / future queue:** Redis
- **Migrations:** Flyway
- **API style:** REST, DTOs + Service layer, centralized error handling

---

## Repo structure
- `backend/` — Spring Boot API
- `infra/` — Docker Compose (Postgres, Redis)
- `simulator/` — Telemetry simulator (planned)
- `docs/` — Architecture docs (planned)

---

## Prerequisites
- Docker + Docker Compose
- Java **21**
- (Optional) `psql` and `jq` for quick testing

---

## Quick start (dev)

### 1. Start infrastructure (Postgres + Redis)
From repo root:
```bash
docker compose -f infra/docker-compose.yml up -d
docker ps
```
#### To reset everything (destroys DB data):
```bash
docker compose -f infra/docker-compose.yml down -v
docker volume rm infra_slo_pgdata 2>/dev/null || true
docker compose -f infra/docker-compose.yml up -d
```

### 2. Run the backend
```bash
cd backend
./gradlew clean bootRun
```
Backend runs on:
- API: http://localhost:8080
- Actuator health: http://localhost:8080/actuator/health

--- 

## Database & migrations (Flyway)
Flyway migrations live in:
- backend/src/main/resources/db/migration/

On startup, the app automatically:
- validates + applies migrations
- fails fast if migration is invalid

Check applied migrations:
```bash
PGPASSWORD=slo psql -h 127.0.0.1 -p 15432 -U slo -d slo -c \
"select installed_rank, version, description, success from flyway_schema_history order by installed_rank;"
```

--- 

## API overview (current)
Base path: /api/v1
Vehicles
- POST   /api/v1/vehicles
- GET    /api/v1/vehicles
- GET    /api/v1/vehicles/{id}

Plate number uniqueness is enforced; duplicates return 409 Conflict.
Example:
```bash
curl -s -X POST http://localhost:8080/api/v1/vehicles \
  -H "Content-Type: application/json" \
  -d '{"plateNumber":"PD123AA","capacityParcels":120,"capacityKg":800,"active":true}' | jq

curl -s http://localhost:8080/api/v1/vehicles | jq
```

Drivers
- POST   /api/v1/drivers
- GET    /api/v1/drivers
- GET    /api/v1/drivers/{id}
- PUT    /api/v1/drivers/{driverId}/vehicle/{vehicleId} (assign)
- DELETE /api/v1/drivers/{driverId}/vehicle (unassign)

Email uniqueness is enforced case-insensitively (lower(email)); duplicates return 409 Conflict.

Example:
```bash
VEH_ID=$(curl -s -X POST http://localhost:8080/api/v1/vehicles \
  -H "Content-Type: application/json" \
  -d '{"plateNumber":"PD111AA","capacityParcels":10,"capacityKg":100,"active":true}' | jq -r .id)

DRV_ID=$(curl -s -X POST http://localhost:8080/api/v1/drivers \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Luigi Bianchi","email":"luigi@demo.it","shiftStart":"08:00:00","shiftEnd":"17:00:00","active":true}' | jq -r .id)

curl -s -X PUT "http://localhost:8080/api/v1/drivers/$DRV_ID/vehicle/$VEH_ID" | jq
curl -s -X DELETE "http://localhost:8080/api/v1/drivers/$DRV_ID/vehicle" | jq
```
Delivery Tasks
- POST   /api/v1/tasks
- GET    /api/v1/tasks
- GET    /api/v1/tasks/{id}

Example:
```bash
curl -s -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"externalRef":"EXT-1","customerName":"Alice","address":"Via Roma 1, Padova","lat":45.4064,"lng":11.8768,"windowStart":"2026-01-28T10:00:00Z","windowEnd":"2026-01-28T12:00:00Z","serviceMinutes":8,"priority":1}' | jq

curl -s http://localhost:8080/api/v1/tasks | jq
```

--- 

## Health check
```bash
curl -s http://localhost:8080/actuator/health | jq
```

--- 

## Error handling
The API uses a centralized exception handler and returns structured errors.
Example (duplicate vehicle plate):
```json
{
  "timestamp": "...",
  "status": 409,
  "error": "Conflict",
  "code": "VEHICLE_PLATE_EXISTS",
  "message": "Vehicle with plateNumber 'PD999ZZ' already exists",
  "path": "/api/v1/vehicles"
}
```

--- 

## Development notes
- Prefer DTOs + service layer (no entities directly in controller I/O)
- Keep database changes in Flyway migrations only
- Use ddl-auto: validate to catch schema drift early

--- 

## Roadmap (next)
- Routing domain (routes + route_stops)
- Task assignment + route building
- Optimization engine integration (OR-Tools)
- Telemetry simulator feeding execution events
- Observability: metrics, tracing, structured logs