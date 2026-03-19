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

1. Install Java 17, Maven, PostgreSQL.
2. Start PostgreSQL and create database: `createdb ecommerce`
3. For each service, run `mvn spring-boot:run` in their directories.
4. Services will run on ports 8081-8084.

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