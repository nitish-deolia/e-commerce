# UML Diagrams

## Class Diagram (Auth Service)
```
User
- id: Long
- username: String
- email: String
- password: String
+ getId(): Long
+ setId(id: Long): void
...

UserRepository
+ findByEmail(email: String): Optional<User>

UserService
+ register(user: User): User
+ login(email: String, password: String): String

AuthController
+ register(user: User): User
+ login(request: LoginRequest): String
```

## Sequence Diagram (User Registration)
1. Client -> AuthController: POST /register
2. AuthController -> UserService: register(user)
3. UserService -> UserRepository: save(user)
4. UserRepository -> Database: save
5. Database -> UserRepository: saved user
6. UserRepository -> UserService: user
7. UserService -> AuthController: user
8. AuthController -> Client: user

## Component Diagram
```
[Client] --> [Auth Service] --> [PostgreSQL]
[Client] --> [Product Service] --> [PostgreSQL]
[Client] --> [Order Service] --> [Inventory Service] --> [PostgreSQL]
```