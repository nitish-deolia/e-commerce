# E-Commerce Backend with Microservices

This project implements a microservices-based e-commerce backend using Java, Spring Boot, and PostgreSQL.

## Services

- **auth-service** (Port 8081): User authentication and registration with JWT.
- **product-service** (Port 8082): Product CRUD operations.
- **order-service** (Port 8083): Order creation and management.
- **inventory-service** (Port 8084): Inventory tracking.

## Tech Stack

- Java 17
- Spring Boot 3.4.0
- PostgreSQL
- Maven
- JWT for authentication

## Setup

1. Install Java 17+ and Maven.
2. Start PostgreSQL with Docker and apply the Liquibase schema:
   `cd db`
   `docker compose up -d postgres`
   `docker compose run --rm liquibase liquibase --url=jdbc:postgresql://postgres:5432/ecommerce --username=postgres --password=postgres123 --changeLogFile=changelog/db.changelog-master.yaml update`
3. Start each service in its own terminal:
   `cd auth-service && mvn spring-boot:run`
   `cd product-service && mvn spring-boot:run`
   `cd order-service && mvn spring-boot:run`
   `cd inventory-service && mvn spring-boot:run`
4. Services run on ports 8081-8084 and connect to the local Docker Postgres instance on `localhost:5432`.

## One-Command Docker Setup

From the repo root:

```bash
docker compose up --build
```

That command:
- starts PostgreSQL
- runs Liquibase migrations
- builds all four microservices
- starts the services on ports `8081`-`8084`
- starts a Swagger landing page on `http://localhost:8090`

To stop everything:

```bash
docker compose down
```

## Swagger UI

Each service exposes Swagger UI:

- Auth Service: `http://localhost:8081/swagger-ui.html`
- Product Service: `http://localhost:8082/swagger-ui.html`
- Order Service: `http://localhost:8083/swagger-ui.html`
- Inventory Service: `http://localhost:8084/swagger-ui.html`

When running the root Docker stack, you can use the landing page at:

- `http://localhost:8090`

## API Endpoints

### Auth Service
- POST /auth/register - Register user
- POST /auth/login - Login user

### Product Service
- GET /products - Get all products
- POST /products - Create product
- PUT /products/{id} - Update product
- DELETE /products/{id} - Delete product

### Order Service
- POST /orders - Create order

### Inventory Service
- GET /inventory/{productId} - Get inventory
- PUT /inventory/{productId}?stock={value} - Update stock

## API Testing

Import the `Ecommerce_Microservices_API.postman_collection.json` file into Postman to test the APIs. The collection includes sample requests for all endpoints with example data.

## CI/CD

GitHub Actions workflow for build and test on push/PR.

## Monitoring

For production, integrate Prometheus and Grafana for metrics.

## Testing

Run `mvn test` in each service directory.

## Deliverables

- Functional microservices with REST APIs
- Database models with JPA
- Basic authentication
- CI/CD pipeline
- Documentation
