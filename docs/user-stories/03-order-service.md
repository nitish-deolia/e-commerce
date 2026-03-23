# Order Service - User Stories

## US-201: Place Order
**As a** customer
**I want to** place an order with selected products
**So that** I can purchase items from the catalog

**Acceptance Criteria:**
- POST `/orders` accepts userId, items array (productId, quantity)
- Validates user authentication (JWT token)
- Calls Inventory Service to check stock availability
- Returns 400 if any item is out of stock
- Creates order with status "PENDING"
- Returns 201 with order ID, items, and total price
- Order total = sum of (price × quantity)

## US-202: View Order History
**As a** customer
**I want to** view my past orders
**So that** I can track my purchase history

**Acceptance Criteria:**
- GET `/orders` returns all orders for authenticated user
- Each order includes: ID, date, status, items, total price
- Supports pagination
- Supports filtering by status (PENDING, CONFIRMED, SHIPPED, DELIVERED)
- Returns 200 with order list
- Returns 401 if not authenticated

## US-203: View Order Details
**As a** customer
**I want to** view detailed information about a specific order
**So that** I can track shipment and verify purchase details

**Acceptance Criteria:**
- GET `/orders/{orderId}` returns complete order details
- Includes: order ID, date, customer info, items, total, status, shipping address
- Returns 404 if order not found
- Returns 403 if user doesn't own the order
- Returns 200 with order details on success

## US-204: Cancel Order
**As a** customer
**I want to** cancel my order before shipment
**So that** I can stop an unwanted purchase

**Acceptance Criteria:**
- DELETE `/orders/{orderId}` cancels order
- Only allows cancellation if status is "PENDING"
- Returns 400 if order already shipped/delivered
- Calls Inventory Service to restore stock
- Updates order status to "CANCELLED"
- Returns 200 success response
- Returns 403 if user doesn't own the order

## US-205: Update Order Status (Admin)
**As an** admin
**I want to** update order status (e.g., mark as shipped)
**So that** customers know their order progress

**Acceptance Criteria:**
- PUT `/orders/{orderId}/status` (admin only) accepts new status
- Valid statuses: PENDING → CONFIRMED → SHIPPED → DELIVERED
- Validates status transition logic
- Returns 200 with updated order
- Returns 400 for invalid status
- Notifies customer of status change (mock)

## US-206: View All Orders (Admin)
**As an** admin
**I want to** view all orders in the system
**So that** I can monitor sales and manage fulfillment

**Acceptance Criteria:**
- GET `/orders/admin/all` (admin only) returns all orders
- Includes filter options: date range, status, customer
- Returns paginated results
- Returns 403 if user is not admin
- Returns 200 with orders list
