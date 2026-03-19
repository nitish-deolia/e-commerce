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

## Technology Stack
- Spring Boot microservices
- PostgreSQL database
- REST APIs with JSON
- JWT for authentication
- Maven for build

## Deployment
- Each service runs independently on different ports (8081-8084).
- Can be containerized with Docker for production.