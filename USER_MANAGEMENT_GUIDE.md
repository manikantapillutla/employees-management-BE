# 🚀 Complete User Management System

## 📋 Overview
The EMS backend now has a complete user management system with registration, login, logout, and profile management.

## 🔗 API Endpoints

### 1. User Registration
```http
POST /api/users/register
Content-Type: application/json

{
    "username": "john_doe",
    "password": "Password123",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890"
}
```

**Response:**
```json
{
    "id": 1,
    "username": "john_doe",
    "role": "USER",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890",
    "message": "User registered successfully",
    "createdAt": "2024-03-12T10:30:00",
    "updatedAt": "2024-03-12T10:30:00"
}
```

### 2. User Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "john_doe",
    "password": "Password123"
}
```

**Response:**
```json
{
    "username": "john_doe",
    "role": "USER",
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "message": "Login successful"
}
```

### 3. User Logout
```http
POST /api/auth/logout
Authorization: Bearer <JWT_TOKEN>
```

**Response:**
```json
"Logged out successfully"
```

### 4. Token Refresh
```http
POST /api/auth/refresh
Authorization: Bearer <JWT_TOKEN>
```

**Response:**
```json
{
    "username": "john_doe",
    "role": "USER",
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "message": "Token refreshed successfully"
}
```

### 5. Get User Profile
```http
GET /api/auth/profile
Authorization: Bearer <JWT_TOKEN>
```

**Response:**
```json
{
    "username": "john_doe",
    "role": "USER",
    "message": "Profile retrieved successfully"
}
```

### 6. Change Password
```http
POST /api/auth/change-password
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
    "currentPassword": "Password123",
    "newPassword": "NewPassword456",
    "confirmPassword": "NewPassword456"
}
```

**Response:**
```json
"Password changed successfully"
```

## 🧪 Testing Commands

### Register a New User
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "TestPass123",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "TestPass123"
  }'
```

### Access Protected Endpoint
```bash
curl -X GET http://localhost:8080/api/employees \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Logout
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

## ✅ Validation Rules

### Username
- Required: Yes
- Length: 3-50 characters
- Pattern: Letters, numbers, underscores only

### Password
- Required: Yes
- Length: 8-100 characters
- Pattern: At least 1 uppercase, 1 lowercase, 1 number

### Email
- Optional: Yes
- Format: Valid email format
- Unique: Yes

### Phone
- Optional: Yes
- Format: Valid phone number with optional country code

## 🔒 Security Features

1. **Password Encryption**: BCrypt hashing
2. **JWT Authentication**: Stateless tokens with 1-hour expiration
3. **Input Validation**: Comprehensive validation on all inputs
4. **Error Handling**: Secure error messages without data exposure
5. **CORS Configuration**: Proper cross-origin setup
6. **Role-Based Access**: ADMIN role for admin endpoints

## 🛡️ Security Configuration

- **Stateless Sessions**: JWT tokens, no server-side session storage
- **Public Endpoints**: `/api/auth/**` and `/api/users/register`
- **Protected Endpoints**: All other endpoints require valid JWT
- **Token Expiration**: 1 hour (configurable)
- **Password Strength**: Enforced complexity requirements

## 🔄 Complete Flow Example

1. **Register User** → Create account with validation
2. **Login** → Authenticate and receive JWT token
3. **Access Protected Resources** → Use JWT token in Authorization header
4. **Refresh Token** → Get new token before expiration
5. **Logout** → Clear security context (client-side token removal)

## 🚨 Error Handling

Common error responses:

```json
{
    "error": "Authentication failed",
    "message": "Invalid username or password",
    "status": "401"
}
```

```json
{
    "error": "Validation failed",
    "message": "Invalid input data",
    "status": "400",
    "validationErrors": {
        "username": "Username must be between 3 and 50 characters",
        "password": "Password must be at least 8 characters with uppercase, lowercase, and number"
    }
}
```

## 🎯 Next Steps

The user management system is now complete and production-ready with:
- ✅ Full registration with validation
- ✅ Secure login with JWT
- ✅ Logout functionality
- ✅ Token refresh mechanism
- ✅ Password change feature
- ✅ Profile management
- ✅ Comprehensive error handling
- ✅ Security best practices

Ready for frontend integration! 🚀
