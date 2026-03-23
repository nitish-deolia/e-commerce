# Database Setup - User Stories

## US-501: Provision Local Database with Docker and Liquibase
**As a** developer
**I want to** start PostgreSQL and apply the schema with Liquibase
**So that** all microservices can run locally against the same database structure

**Acceptance Criteria:**
- PostgreSQL can be started locally with Docker Compose
- Liquibase applies all changelog files to the `ecommerce` database
- All objects are created under the `ecommerce` schema
- Seed data is available after migration
- The setup works on macOS, Windows, and Linux with Docker
- Database setup can be run either from `db/` or from the repo root Docker stack

## US-502: Bring Up Full Local Stack
**As a** developer
**I want to** start the database, migrations, and all services together
**So that** I can verify the whole backend quickly in one environment

**Acceptance Criteria:**
- Root `docker compose up --build` starts PostgreSQL, Liquibase, and all four services
- Services connect to PostgreSQL using the `ecommerce` schema
- Auth, Product, Order, and Inventory services expose health endpoints on ports `8081`-`8084`
- Product service returns seeded products from the migrated database
- Auth service can log in with seeded admin data
- Order service can create an order and reduce inventory through service-to-service calls

## US-503: Verify Local Database Readiness
**As a** developer
**I want to** confirm that the local database is ready before debugging services
**So that** I can quickly isolate whether failures are in the DB setup or application code

**Acceptance Criteria:**
- Developers can verify the database using `docker compose exec postgres psql -U postgres -d ecommerce`
- Developers can verify applied migrations using Liquibase status/history commands
- Documentation includes the local connection string for apps and DB clients
- Documentation includes a minimal troubleshooting path for connection or migration failures
