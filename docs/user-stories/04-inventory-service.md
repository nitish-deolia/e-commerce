# Inventory Service - User Stories

## US-301: Check Product Stock
**As the** order service
**I want to** check if a product has sufficient stock
**So that** I can validate orders before processing

**Acceptance Criteria:**
- GET `/inventory/{productId}` returns current stock level
- Returns: productId, quantity, reorderLevel, warehouse location
- Returns 200 with stock data if product exists
- Returns 404 if product not found
- No authentication required (internal service call)

## US-302: Update Stock After Order
**As the** order service
**I want to** reduce inventory when an order is placed
**So that** stock levels remain accurate

**Acceptance Criteria:**
- PUT `/inventory/{productId}` accepts quantityChange (negative for reduction)
- Validates sufficient stock exists before reduction
- Returns 400 if insufficient stock
- Returns 200 with updated stock level
- Updates stock atomically (no race conditions)
- Logs all stock changes with timestamp

## US-303: Restore Stock on Order Cancellation
**As the** order service
**I want to** restore inventory when an order is cancelled
**So that** cancelled items become available for other customers

**Acceptance Criteria:**
- PUT `/inventory/{productId}` accepts positive quantityChange
- Adds quantity back to stock
- Returns 200 with updated stock level
- Validates quantityChange is positive
- Logs restoration with reason code

## US-304: View Stock History
**As an** admin
**I want to** view the history of stock changes for a product
**So that** I can audit inventory movements

**Acceptance Criteria:**
- GET `/inventory/{productId}/history` returns stock change log
- Includes: timestamp, quantity changed, reason, order reference
- Supports date range filtering
- Returns paginated results
- Returns 200 with history
- Returns 404 if product not found

## US-305: Set Reorder Threshold
**As an** admin
**I want to** set minimum stock level (reorderLevel) for products
**So that** I get alerted when stock runs low

**Acceptance Criteria:**
- PUT `/inventory/{productId}/reorder-level` accepts new threshold
- Validates threshold is positive number
- Returns 200 with updated configuration
- Returns 404 if product not found
- Alerts triggered when stock < reorderLevel

## US-306: Low Stock Alert
**As the** system
**I want to** track when product stock falls below reorderLevel
**So that** admins are notified to restock

**Acceptance Criteria:**
- Automatic alert generated when stock < reorderLevel
- Alert includes: productId, current stock, reorderLevel
- Alert stored in log for admin review
- GET `/inventory/alerts` returns pending low-stock alerts
- Admin can mark alert as acknowledged
