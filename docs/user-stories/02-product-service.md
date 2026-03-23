# Product Service - User Stories

## US-101: Browse All Products
**As a** customer
**I want to** view all available products
**So that** I can explore what's available for purchase

**Acceptance Criteria:**
- GET `/products` returns list of all products
- Each product includes: ID, name, description, price, category
- Returns paginated results (default 20 items per page)
- Supports filtering by category
- Supports sorting by price, rating, newest
- Returns 200 with empty array if no products exist

## US-102: View Product Details
**As a** customer
**I want to** view detailed information about a specific product
**So that** I can make an informed purchase decision

**Acceptance Criteria:**
- GET `/products/{productId}` returns full product details
- Includes: ID, name, description, price, category, specifications, ratings, reviews
- Includes current stock availability status
- Returns 404 if product not found
- Returns 200 with product object on success

## US-103: Create Product (Admin)
**As an** admin
**I want to** add new products to the catalog
**So that** customers can purchase them

**Acceptance Criteria:**
- POST `/products` (admin only) accepts product details
- Required fields: name, description, price, category
- Optional fields: specifications, images, ratings
- Validates price (positive number)
- Returns 201 with created product ID
- Returns 403 if user is not admin
- Returns 400 for validation errors

## US-104: Update Product (Admin)
**As an** admin
**I want to** update product information
**So that** keep product details current and accurate

**Acceptance Criteria:**
- PUT `/products/{productId}` (admin only) accepts product updates
- Can update any field (name, price, description, category)
- Validates data before update
- Returns 200 with updated product
- Returns 404 if product not found
- Returns 403 if user is not admin

## US-105: Delete Product (Admin)
**As an** admin
**I want to** remove products from the catalog
**So that** outdated or discontinued items are no longer available

**Acceptance Criteria:**
- DELETE `/products/{productId}` (admin only)
- Soft delete (mark as inactive) instead of hard delete
- Returns 204 No Content on success
- Returns 404 if product not found
- Returns 403 if user is not admin

## US-106: Search Products
**As a** customer
**I want to** search for products by keyword
**So that** I can quickly find what I'm looking for

**Acceptance Criteria:**
- GET `/products/search?keyword=xyz` returns matching products
- Searches product name and description
- Case-insensitive matching
- Returns paginated results
- Returns empty array if no matches found
