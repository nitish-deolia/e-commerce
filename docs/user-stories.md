# User Stories and Acceptance Criteria

## Auth Service
- **As a new user, I want to register so that I can access the system.**
  - Acceptance: POST /auth/register with username, email, password returns success.

- **As a user, I want to login so that I can get authenticated.**
  - Acceptance: POST /auth/login with email, password returns JWT token.

## Product Service
- **As an admin, I want to add products so that users can browse them.**
  - Acceptance: POST /products with product details creates and returns product.

- **As a user, I want to view all products so that I can shop.**
  - Acceptance: GET /products returns list of products.

## Order Service
- **As a user, I want to place an order so that I can purchase products.**
  - Acceptance: POST /orders with userId, items creates order and updates inventory.

## Inventory Service
- **As the system, I want to track stock so that orders don't exceed availability.**
  - Acceptance: PUT /inventory/{productId} updates stock, GET returns current stock.