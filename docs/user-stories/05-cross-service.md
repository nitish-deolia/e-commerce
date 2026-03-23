# Cross-Service User Stories

## US-401: Complete Order Flow (Integration)
**As a** customer
**I want to** complete a full end-to-end order
**So that** I can successfully purchase products

**Acceptance Criteria:**
- Customer registers and logs in (Auth Service)
- Customer browses and finds product (Product Service)
- Customer places order with product (Order Service)
- Order Service calls Inventory Service to check stock
- Order Service calls Inventory Service to reduce stock
- Order created successfully with status "CONFIRMED"
- Returns 201 with complete order details
- All services respond within SLA (< 2 seconds total)

## US-402: Order Failure Due to Stock
**As a** customer placing an order
**I want to** receive clear feedback if items are out of stock
**So that** I understand why my order failed

**Acceptance Criteria:**
- POST `/orders` fails if Product Service returns stock = 0
- Inventory Service confirms stock unavailability
- Returns 400 Bad Request with error message
- Error message specifies which product is out of stock
- Suggests waitlist option (future feature)

## US-403: Service Communication Fallback
**As the** system
**I want to** handle service call failures gracefully
**So that** user experience is not severely impacted

**Acceptance Criteria:**
- If Inventory Service is down, order creation returns 503 (retry message)
- If Product Service is unavailable, product list shows cached data
- Circuit breaker pattern implemented (3 failures → fallback mode)
- Fallback mode allows reads but defers writes
- Services recover automatically (retry every 30 seconds)
- Error response includes retry suggestion

## US-404: Cross-Service Logging
**As a** developer
**I want to** trace requests across multiple services
**So that** I can debug issues and monitor performance

**Acceptance Criteria:**
- Each request gets unique correlation ID (X-Correlation-ID header)
- All services log with correlation ID
- Central logging aggregates logs from all services
- Can search logs by correlation ID
- Performance metrics exported to Prometheus
- Dashboard shows average response time per service

## US-405: Data Consistency (Saga Pattern)
**As the** system
**I want to** maintain data consistency across services
**So that** no inconsistencies exist (e.g., order placed but inventory not updated)

**Acceptance Criteria:**
- Order placement triggers compensating transaction
- If inventory update fails, order is automatically cancelled
- If product service fails, inventory is restored
- Status field in Order Service tracks saga state (PENDING, RESERVING_INVENTORY, CONFIRMED)
- Manual intervention available for stuck transactions
- Saga logs stored for audit
