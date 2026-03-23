# Sequence Diagrams

## User Registration Flow

```
Customer    AuthService    Database    EmailService
   │            │             │            │
   │ POST /register           │            │
   ├───────────────────────────>            │
   │            │             │            │
   │            │  Validate   │            │
   │            ├─────────────>            │
   │            │             │            │
   │            │  Hash Password           │
   │            ├─────────────────────────────┐
   │            │             │            │ │
   │            │  Insert User│            │ │
   │            ├─────────────>            │ │
   │            │             │            │ │
   │            │  Generate Verify Token  │
   │            ├─────────────────────────────┐
   │            │             │            │ │
   │            │  Send Verification Email   │
   │            ├───────────────────────────────>
   │            │             │            │
   │            │ Return 201  │            │
   │<───────────┤             │            │
   │ Success    │             │            │
```

## Order Placement Flow

```
Customer    OrderService    ProductService    InventoryService    Database
   │            │                 │                  │                │
   │ POST /orders                 │                  │                │
   ├───────────────────────────────>                │                │
   │            │                 │                  │                │
   │            │ Get Product Details               │                │
   │            ├─────────────────────>             │                │
   │            │                 │                  │                │
   │            │ Response        │                  │                │
   │            │<─────────────────                 │                │
   │            │                 │                  │                │
   │            │ Check Stock Available              │                │
   │            ├──────────────────────────────────> │                │
   │            │                 │                  │                │
   │            │                 │  Query Current Stock             │
   │            │                 │                  ├───────────────>
   │            │                 │                  │                │
   │            │                 │                  │ Return Qty    │
   │            │                 │                  │<────────────────
   │            │                 │ Return Stock    │
   │            │<──────────────────────────────────│                │
   │            │                 │                  │                │
   │      [Sufficient Stock?]      │                  │                │
   │            │                 │                  │                │
   │            │ Update Stock (decrement)           │                │
   │            ├──────────────────────────────────> │                │
   │            │                 │                  │                │
   │            │                 │      Update Quantity            │
   │            │                 │                  ├───────────────>
   │            │                 │                  │                │
   │            │                 │ Acknowledge      │                │
   │            │                 │<──────────────────────────────────
   │            │                 │                  │                │
   │            │ Save Order       │                  │                │
   │            ├───────────────────────────────────────────────────>
   │            │                 │                  │                │
   │            │                 │                  │  Return 201   │
   │<───────────┤                 │                  │                │
   │ 201 Created│                 │                  │                │
```

## Stock Management and Low Stock Alert

```
System    InventoryService    ProductService    AdminNotification    Database
   │            │                  │                    │                │
   │ Scheduled Check               │                    │                │
   │ (Every 1 hour)                │                    │                │
   ├───────────────>               │                    │                │
   │            │                  │                    │                │
   │            │ Get All Products │                    │                │
   │            ├──────────────────────────────────────────────────────>
   │            │                  │                    │                │
   │            │                  │ Return Product List               │
   │            │                  │                    │<────────────────
   │            │                  │                    │                │
   │     [For Each Product]         │                    │                │
   │            │                  │                    │                │
   │            │ Get Current Stock │                    │                │
   │            ├──────────────────────────────────────────────────────>
   │            │                  │                    │                │
   │            │                  │                    │  Return Stock  │
   │            │                  │                    │<────────────────
   │            │                  │                    │                │
   │        [Stock < ReorderLevel?] │                    │                │
   │            │                  │                    │                │
   │            │ Create Alert     │                    │                │
   │            ├──────────────────────────────────────────────────────>
   │            │                  │                    │                │
   │            │ Create Notification                   │                │
   │            ├──────────────────────────────────────>                │
   │            │                  │                    │                │
   │            │                  │ Send Email/SMS     │                │
   │            │                  │                    │                │
```

## Cross-Service Error Handling (Circuit Breaker)

```
OrderService    InventoryService    Circuit Breaker    Fallback
   │                  │                   │                │
   │ Request 1        │                   │                │
   ├──────────────────>  CLOSED (working)│                │
   │                  │                   │                │
   │ Response 1       │<──────────────────│                │
   │<──────────────────                   │                │
   │                  │                   │                │
   │ Request 2        │                   │                │
   ├──────────────────>                   │                │
   │                  X (Failure)         │                │
   │                  │                   │                │
   │                  │   Count Failure   │                │
   │                  │<──────────────────│                │
   │                  │                   │                │
   │ Request 3        │                   │                │
   ├──────────────────>  [Threshold Reached]              │
   │                  │   OPEN            │                │
   │                  │<──────────────────│                │
   │                  │                   │ Fallback       │
   │                  │                   ├───────────────>
   │                  │                   │ Cached Data    │
   │                  │                   │<───────────────┤
   │ Response (Cached)│<──────────────────────────────────│
   │<──────────────────                   │                │
   │                  │ [Wait 30 seconds] │                │
   │                  │                   │                │
   │   Retry Timer    │                   HALF_OPEN       │
   │                  │   Single Request  │                │
   │ Request 4        │<──────────────────│                │
   ├──────────────────>                   │                │
   │                  │ Success           │                │
   │                  │<──────────────────│                │
   │                  │                   CLOSED          │
   │ Response         │<──────────────────│                │
   │<──────────────────                   │                │
```
