# Database Setup with Docker & Liquibase

## Overview
This folder gives you a local PostgreSQL instance plus Liquibase migrations for the app schema.

## Prerequisites
- Docker Desktop or Docker Engine with `docker compose`

## 🚀 Quick Start

```bash
cd db
docker compose up -d postgres
# wait a few seconds for PostgreSQL to become ready
docker compose run --rm liquibase liquibase --url=jdbc:postgresql://postgres:5432/ecommerce --username=postgres --password=postgres123 --changeLogFile=changelog/db.changelog-master.yaml update
```

## 📡 Verify Setup

```bash
docker compose exec postgres psql -U postgres -d ecommerce

\dt
\q
```

## Manual Liquibase Commands

```bash
docker compose run --rm liquibase liquibase status
```

```bash
docker compose run --rm liquibase liquibase updateSQL
```

```bash
docker compose run --rm liquibase liquibase history
```

```bash
docker compose run --rm liquibase liquibase rollbackCount 1
```

## Stopping & Cleanup

```bash
docker compose down
```

```bash
docker compose down -v
```

```bash
docker compose logs postgres
docker compose logs liquibase
```

## 🔧 Troubleshooting

- If `docker compose` is not available, install or start Docker Desktop and reopen your terminal.
- If port `5432` is already in use, stop the conflicting service or change the port mapping in [docker-compose.yml](/Users/nitish.deolia/Desktop/Nitish/Backend/db/docker-compose.yml).
- If migration fails with connection errors, wait a few seconds and rerun the Liquibase command.
- To inspect errors, run:

```bash
docker compose logs liquibase -f
```
