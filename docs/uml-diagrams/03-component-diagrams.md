# Component Diagrams

## High-Level System Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          CLIENT LAYER                                   │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  Web Browser / Mobile App / Third-party Integrations           │   │
│  └────────────────────────┬─────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
                             │ REST/HTTP
                             ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      API GATEWAY LAYER                                  │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │ API Gateway (Port 8000)                                         │   │
│  │ • Request Routing                                               │   │
│  │ • Rate Limiting                                                 │   │
│  │ • Authentication (JWT Validation)                               │   │
│  │ • Request Logging & Monitoring                                  │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
      │              │              │               │
      ▼              ▼              ▼               ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌────────────────┐
│ Auth Service │ │Product Service│ │ Order Service│ │Inventory Service│
│  (Port 8081) │ │ (Port 8082)  │ │ (Port 8083) │ │  (Port 8084)   │
├──────────────┤ ├──────────────┤ ├──────────────┤ ├────────────────┤
│Microservices │ │Microservices │ │Microservices │ │Microservices   │
│              │ │              │ │              │ │                │
│ Controllers  │ │ Controllers  │ │ Controllers  │ │ Controllers    │
│ Services     │ │ Services     │ │ Services     │ │ Services       │
│ Repositories │ │ Repositories │ │ Repositories │ │ Repositories   │
└──────────────┘ └──────────────┘ └──────────────┘ └────────────────┘
      │              │              │               │
      └──────────────┼──────────────┼───────────────┘
                     │
                     ▼
         ┌──────────────────────────┐
         │  PostgreSQL Database     │
         │  (Port 5432)             │
         │                          │
         │  Schemas:                │
         │  • ecommerce_schema      │
         │    - users               │
         │    - products            │
         │    - orders              │
         │    - inventory           │
         │    - order_items         │
         └──────────────────────────┘
                     │
                     ▼
         ┌──────────────────────────┐
         │  Persistence Layer       │
         │  • Connection Pool       │
         │  • Query Optimization    │
         │  • Caching (Redis)       │
         └──────────────────────────┘
```

## Microservice Internal Components

```
┌─────────────────────────────────────────────────────────┐
│              Auth Service (8081)                        │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────────────────────────────────────────┐  │
│  │ Presentation Layer (Controllers)                 │  │
│  │  • UserController                               │  │
│  │    - POST /auth/register                        │  │
│  │    - POST /auth/login                           │  │
│  │    - POST /auth/refresh                         │  │
│  │  • AuthenticationFilter                         │  │
│  └──────────────────────────────────────────────────┘  │
│           │                                             │
│           ▼                                             │
│  ┌──────────────────────────────────────────────────┐  │
│  │ Business Logic Layer (Services)                  │  │
│  │  • UserService                                  │  │
│  │    - register()                                 │  │
│  │    - login()                                    │  │
│  │    - validateToken()                            │  │
│  │  • PasswordEncryptionService                    │  │
│  │  • JWTTokenProvider                             │  │
│  └──────────────────────────────────────────────────┘  │
│           │                                             │
│           ▼                                             │
│  ┌──────────────────────────────────────────────────┐  │
│  │ Data Access Layer (Repositories)                 │  │
│  │  • UserRepository (JPA)                         │  │
│  │  • TokenRepository (JPA)                        │  │
│  └──────────────────────────────────────────────────┘  │
│           │                                             │
│           └─────────┬──────────┐                        │
│                     │          │                        │
│                     ▼          ▼                        │
│           ┌──────────────┐  ┌──────────────┐           │
│           │ PostgreSQL   │  │  Redis Cache │           │
│           │ (users, auth)│  │  (sessions)  │           │
│           └──────────────┘  └──────────────┘           │
│                                                         │
│  ┌──────────────────────────────────────────────────┐  │
│  │ Cross-Cutting Concerns                          │  │
│  │  • Exception Handling                           │  │
│  │  • Logging                                      │  │
│  │  • Security (JWT, CORS)                         │  │
│  │  • Metrics (Micrometer)                         │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## Order Service with Inter-Service Call

```
┌──────────────────────────────────────────────────────────────────┐
│                  Order Service (8083)                            │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ OrderController                                            │ │
│  │  • POST /orders (placeOrder)                               │ │
│  │  • GET /orders/{orderId}                                   │ │
│  │  • PUT /orders/{orderId}/status                            │ │
│  └────────────────────────────────────────────────────────────┘ │
│           │                                                      │
│           ▼                                                      │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ OrderService                                               │ │
│  │  • placeOrder(userId, items) {                             │ │
│  │      1. Validate user                                      │ │
│  │      2. Call ProductService.getProductDetails()           │ │
│  │      3. Call InventoryService.checkStock()                │ │
│  │      4. Call InventoryService.updateStock()               │ │
│  │      5. Create Order record                               │ │
│  │      6. Return results                                    │ │
│  │    }                                                       │ │
│  └────────────────────────────────────────────────────────────┘ │
│           │                                                      │
│  ┌────────┴────────────────────────────────────────────────────┐ │
│  │ External Service Calls (via REST Client)                    │ │
│  │                                                             │ │
│  │  ┌─────────────────────────┐   ┌─────────────────────────┐ │ │
│  │  │ ProductService Client   │   │ InventoryService Client │ │ │
│  │  │ • baseURL: 8082         │   │ • baseURL: 8084         │ │ │
│  │  │ • timeout: 5s           │   │ • timeout: 5s           │ │ │
│  │  │ • circuit-breaker: yes  │   │ • circuit-breaker: yes  │ │ │
│  │  │ • retry: 3 attempts     │   │ • retry: 3 attempts     │ │ │
│  │  └─────────────────────────┘   └─────────────────────────┘ │ │
│  └────────────────────────────────────────────────────────────┘ │
│           │                                                      │
│           ▼                                                      │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ OrderRepository                                            │ │
│  │  • save(order)                                             │ │
│  │  • findById(orderId)                                       │ │
│  │  • findByUserId(userId)                                    │ │
│  └────────────────────────────────────────────────────────────┘ │
│           │                                                      │
│           ▼                                                      │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ PostgreSQL Database                                        │ │
│  │  • orders table                                            │ │
│  │  • order_items table                                       │ │
│  └────────────────────────────────────────────────────────────┘ │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

## Monitoring & Logging Architecture

```
┌──────────────────────────────────────────────────────────────┐
│               Monitoring & Observability                     │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  All Microservices                                           │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ • Spring Boot Actuator                               │ │
│  │ • Micrometer Metrics                                 │ │
│  │ • Log4j / SLF4J with Correlation IDs                 │ │
│  				───────│────────────                              │
│  └────────────────────────────────────────────────────────┘ │
│           │                      │                           │
│           ▼                      ▼                           │
│  ┌──────────────────┐  ┌──────────────────┐               │
│  │   Prometheus     │  │  Loki / ELK      │               │
│  │   (Metrics)      │  │  (Logs)          │               │
│  │  :9090           │  │  :3100           │               │
│  └────────┬─────────┘  └────────┬─────────┘               │
│           │                     │                          │
│           └──────────┬──────────┘                          │
│                      ▼                                     │
│           ┌────────────────────┐                          │
│           │  Grafana           │                          │
│           │  Dashboards        │                          │
│           │  Alerts            │                          │
│           │  :3000             │                          │
│           └────────────────────┘                          │
│                                                            │
└──────────────────────────────────────────────────────────────┘
```

## Deployment Architecture

```
┌──────────────────────────────────────────────────────────────────────┐
│                     Development Environment                          │
├──────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Docker Compose (docker-compose.yml)                               │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  Services:                                                   │  │
│  │  • auth-service (port 8081)                                 │  │
│  │  • product-service (port 8082)                              │  │
│  │  • order-service (port 8083)                                │  │
│  │  • inventory-service (port 8084)                            │  │
│  │  • postgres (port 5432)                                     │  │
│  │  • redis (port 6379) [optional]                             │  │
│  │  • prometheus (port 9090)                                   │  │
│  │  • grafana (port 3000)                                      │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
│  Network: ecommerce-network                                         │
│  Volume: ecommerce-postgres-data                                    │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  GitHub Actions CI/CD Pipeline                              │  │
│  │  1. Trigger: push to main/develop branches                 │  │
│  │  2. Build: Maven clean package for each service           │  │
│  │  3. Test: Run unit & integration tests                     │  │
│  │  4. SonarQube: Code quality analysis                       │  │
│  │  5. Docker: Build & push images to registry               │  │
│  │  6. Deploy: (future) Deploy to cloud platform             │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘
```
