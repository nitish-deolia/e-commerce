# Auth Service - User Stories

## US-001: User Registration
**As a** new user
**I want to** create an account with email and password
**So that** I can access the e-commerce platform

**Acceptance Criteria:**
- POST `/auth/register` accepts `username`, `email`, `password`
- Email validation (valid format, unique)
- Password validation (minimum 8 characters, contains alphanumeric + special char)
- Returns success message with user ID
- Returns appropriate error messages for validation failures
- Passwords are securely hashed (bcrypt)

## US-002: User Login
**As a** registered user
**I want to** login with my credentials
**So that** I can access my account and make purchases

**Acceptance Criteria:**
- POST `/auth/login` accepts `email` and `password`
- Returns JWT token with 24-hour expiration on success
- Returns 401 Unauthorized for invalid credentials
- JWT includes user ID and email as claims
- Token can be used for authenticated API calls

## US-003: Token Refresh
**As a** user with an expiring token
**I want to** refresh my authentication token
**So that** I can maintain my session without re-logging in

**Acceptance Criteria:**
- POST `/auth/refresh` accepts current JWT token
- Returns new JWT token with extended expiration
- Returns 401 if token is invalid or expired
- Old token remains valid for 1 minute after refresh

## US-004: User Logout
**As a** logged-in user
**I want to** logout from my account
**So that** my session is terminated securely

**Acceptance Criteria:**
- POST `/auth/logout` invalidates the current JWT token
- Returns 200 success response
- Subsequent API calls with old token return 401

## US-005: Password Reset
**As a** user who forgot their password
**I want to** reset my password securely
**So that** I can regain access to my account

**Acceptance Criteria:**
- POST `/auth/forgot-password` accepts email
- Generates reset token (valid for 1 hour)
- Sends reset link via email (mock implementation)
- PUT `/auth/reset-password` accepts token and new password
- New password validated before update
- Returns success message after password update
