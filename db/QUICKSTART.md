# Database Setup - Quick Reference

**Cross-platform Docker setup - works on Windows, macOS, and Linux!**

## ⚡ Quick Start

### Any Platform
```bash
cd db
docker compose up -d postgres
# wait a few seconds for PostgreSQL to become ready
docker compose run --rm liquibase liquibase --url=jdbc:postgresql://postgres:5432/ecommerce --username=postgres --password=postgres123 --changeLogFile=changelog/db.changelog-master.yaml update
```

## ✅ Verify

```bash
docker compose exec postgres psql -U postgres -d ecommerce
\dt
\q
```

See `README.md` for troubleshooting and extra Liquibase commands.
