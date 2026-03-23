# Class Diagrams

## Auth Service Class Diagram

```
┌─────────────────────────┐
│        User             │
├─────────────────────────┤
│ - userId: UUID          │
│ - username: String      │
│ - email: String         │
│ - passwordHash: String  │
│ - role: Role            │
│ - createdAt: Date       │
│ - updatedAt: Date       │
├─────────────────────────┤
│ + register()            │
│ + login()               │
│ + updateProfile()       │
│ + resetPassword()       │
└─────────────────────────┘
         │
         │ has
         ▼
┌─────────────────────────┐
│        Role             │
├─────────────────────────┤
│ - roleId: UUID          │
│ - name: String          │
│ - permissions: List     │
├─────────────────────────┤
│ + canCreate()           │
│ + canRead()             │
│ + canUpdate()           │
│ + canDelete()           │
└─────────────────────────┘

┌─────────────────────────┐
│    AuthToken            │
├─────────────────────────┤
│ - tokenId: UUID         │
│ - userId: UUID          │
│ - token: String         │
│ - expiresAt: Date       │
│ - refreshToken: String  │
├─────────────────────────┤
│ + generateToken()       │
│ + validateToken()       │
│ + refreshToken()        │
│ + revokeToken()         │
└─────────────────────────┘
```

## Product Service Class Diagram

```
┌─────────────────────────┐
│      Product            │
├─────────────────────────┤
│ - productId: UUID       │
│ - name: String          │
│ - description: String   │
│ - price: BigDecimal     │
│ - category: Category    │
│ - isActive: Boolean     │
│ - createdAt: Date       │
├─────────────────────────┤
│ + create()              │
│ + update()              │
│ + delete()              │
│ + getDetails()          │
└─────────────────────────┘
         │
         │ has 1..1
         ▼
┌─────────────────────────┐
│      Category           │
├─────────────────────────┤
│ - categoryId: UUID      │
│ - name: String          │
│ - description: String   │
├─────────────────────────┤
│ + create()              │
│ + update()              │
│ + getProducts()         │
└─────────────────────────┘

┌─────────────────────────┐
│      Review             │
├─────────────────────────┤
│ - reviewId: UUID        │
│ - productId: UUID       │
│ - userId: UUID          │
│ - rating: Int (1-5)     │
│ - comment: String       │
│ - createdAt: Date       │
├─────────────────────────┤
│ + create()              │
│ + delete()              │
│ + getAvgRating()        │
└─────────────────────────┘
```

## Order Service Class Diagram

```
┌─────────────────────────┐
│        Order            │
├─────────────────────────┤
│ - orderId: UUID         │
│ - userId: UUID          │
│ - orderDate: Date       │
│ - status: OrderStatus   │
│ - totalPrice: BigDecimal│
│ - items: List<OrderItem>│
├─────────────────────────┤
│ + create()              │
│ + cancel()              │
│ + updateStatus()        │
│ + getTotal()            │
└─────────────────────────┘
         │
         │ contains 1..*
         ▼
┌─────────────────────────┐
│      OrderItem          │
├─────────────────────────┤
│ - itemId: UUID          │
│ - orderId: UUID         │
│ - productId: UUID       │
│ - quantity: Int         │
│ - unitPrice: BigDecimal │
│ - subtotal: BigDecimal  │
├─────────────────────────┤
│ + getSubtotal()         │
│ + update()              │
└─────────────────────────┘

┌──────────────────────────┐
│    OrderStatus (ENUM)    │
├──────────────────────────┤
│ PENDING                  │
│ CONFIRMED                │
│ SHIPPED                  │
│ DELIVERED                │
│ CANCELLED                │
└──────────────────────────┘
```

## Inventory Service Class Diagram

```
┌─────────────────────────┐
│       Stock             │
├─────────────────────────┤
│ - stockId: UUID         │
│ - productId: UUID       │
│ - quantity: Int         │
│ - reorderLevel: Int     │
│ - warehouse: String     │
│ - updatedAt: Date       │
├─────────────────────────┤
│ + checkAvailability()   │
│ + reduceStock()         │
│ + restoreStock()        │
│ + updateReorderLevel()  │
│ + isLowStock()          │
└─────────────────────────┘
         │
         │ logs to
         ▼
┌─────────────────────────┐
│    StockHistory         │
├─────────────────────────┤
│ - historyId: UUID       │
│ - stockId: UUID         │
│ - quantityChanged: Int  │
│ - reason: String        │
│ - orderId: UUID         │
│ - timestamp: Date       │
├─────────────────────────┤
│ + getHistory()          │
│ + auditLog()            │
└─────────────────────────┘

┌──────────────────────────┐
│   StockAlert (ENUM)      │
├──────────────────────────┤
│ LOW_STOCK                │
│ OUT_OF_STOCK             │
│ OVERSTOCK                │
└──────────────────────────┘
```
