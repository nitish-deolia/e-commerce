# Architecture Overview

## Microservice Boundaries
- **Auth Service**: Handles user registration, login, and JWT token generation. Manages user data.
- **Product Service**: Manages product catalog with CRUD operations.
- **Order Service**: Processes orders, calculates totals, and communicates with inventory.
- **Inventory Service**: Tracks product stock levels.

## Data Flow
1. User registers/logs in via Auth Service → receives JWT.
2. User browses products via Product Service.
3. User places order via Order Service → Order Service calls Inventory Service to check/update stock.
4. All services connect to shared PostgreSQL database (ecommerce schema).

## Database Architecture
- PostgreSQL runs locally in Docker
- Liquibase manages schema creation and seed data
- Database objects live in the `ecommerce` schema
- Services use JPA against the Liquibase-managed schema instead of generating tables on startup
- Root Docker Compose can start PostgreSQL, run Liquibase, and launch all services together

## Technology Stack
- Spring Boot microservices
- PostgreSQL database
- Liquibase migrations
- Docker Compose for local orchestration
- REST APIs with JSON
- JWT for authentication
- Maven for build

## Deployment
- Each service runs independently on different ports (8081-8084).
- Local development supports both:
  - running services individually with `mvn spring-boot:run`
  - running the full stack with root `docker compose up --build`
