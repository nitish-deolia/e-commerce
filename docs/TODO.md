# Deliverables TODO List

## 1. Design Documentation
- [x] Create UML Class Diagrams for each microservice
- [x] Create Sequence Diagrams for key flows (e.g., user registration, order creation)
- [x] Create Component Diagram for microservice architecture
- [x] Document architecture overview (boundaries, data flow)
- [x] List user stories and acceptance criteria

## 2. Functional Codebase Enhancements
- [x] Add input validation and error handling to controllers
- [x] Implement inter-service communication (e.g., order calls inventory and product services)
- [x] Add proper HTTP status codes and responses

## 3. Testing Suite
- [x] Add unit tests for UserService (auth-service)
- [x] Add unit tests for ProductService (product-service)
- [ ] Add unit tests for OrderService (order-service)
- [ ] Add unit tests for InventoryService (inventory-service)
- [ ] Add integration tests for API endpoints
- [ ] Ensure ≥50% test coverage (run Jacoco report)

## 4. CI/CD Setup Enhancement
- [ ] Update GitHub Actions workflow with deployment steps
- [ ] Add artifact build and upload

## 5. Monitoring & Logging
- [x] Add Spring Boot Actuator for metrics
- [ ] Configure Prometheus endpoints
- [ ] Create basic Grafana dashboard setup instructions

## 6. Documentation
- [ ] Add Swagger/OpenAPI for API docs
- [x] Update README with detailed API endpoints
- [ ] Add PR summary examples

## 7. Comparative Analysis
- [x] Create comparative-analysis.md with AI vs manual code examples
- [x] Document productivity and quality insights

## Validation
- [x] Run full build and tests (auth-service tested)
- [x] Verify APIs work (services started, Postman collection created)
- [ ] Check coverage report