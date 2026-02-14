# 🧪 Backend API Testing Guide - Start Here

## 🎯 **WHERE TO START TESTING**

### **📋 Current Status Check:**

Your `application.properties` shows database configuration is **commented out**:
```properties
#spring.datasource.url=jdbc:mysql://localhost:3306/employee_management_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
#spring.datasource.username=root
#spring.datasource.password=password
```

---

## 🚀 **STEP 1: ENABLE DATABASE CONNECTION**

### **Uncomment Database Configuration:**
```properties
# Remove the # from these lines:
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### **Enable JPA Auto-Creation:**
```properties
# Uncomment this line to create tables automatically:
spring.jpa.hibernate.ddl-auto=update
```

---

## 🗄️ **STEP 2: DATABASE SETUP OPTIONS**

### **Option A: Empty Database (Fresh Start)**
```sql
-- Create fresh database
DROP DATABASE IF EXISTS employee_management_db;
CREATE DATABASE employee_management_db;
USE employee_management_db;

-- Let Spring Boot create tables automatically
-- Tables will be empty initially
```

### **Option B: Import Mock Data (Pre-populated)**
```sql
-- Use existing database with mock data
USE employee_management_db;

-- Import mock data (if tables are empty)
source f:\Profolio_Projects\employee-management-ui\mock-data\complete_schema.sql;
source f:\Profolio_Projects\employee-management-ui\mock-data\employees.sql;
source f:\Profolio_Projects\employee-management-ui\mock-data\departments.sql;
source f:\Profolio_Projects\employee-management-ui\mock-data\attendance.sql;
source f:\Profolio_Projects\employee-management-ui\mock-data\task_management.sql;
```

---

## 🎯 **STEP 3: START BACKEND & TEST APIS**

### **Start Backend:**
```bash
cd f:\Profolio_Projects\employee-backend
mvn spring-boot:run
```

### **API Testing Order (Start to End):**

#### **🔍 1. HEALTH CHECK**
```bash
# Test if backend is running
GET http://localhost:8080/actuator/health
```
**Expected Response:**
```json
{
  "status": "UP"
}
```

#### **👥 2. CREATE FIRST EMPLOYEE**
```bash
# Create employee to test empty database
POST http://localhost:8080/api/employees
Content-Type: application/json

{
  "firstName": "Test",
  "lastName": "User",
  "email": "test.user@company.com",
  "jobTitle": "Software Engineer",
  "departmentId": 1,
  "salary": 75000.0,
  "status": "active"
}
```

#### **🏢 3. CREATE DEPARTMENT**
```bash
# Create department first (if needed)
POST http://localhost:8080/api/departments
Content-Type: application/json

{
  "name": "Engineering",
  "description": "Software Development Team",
  "managerId": 1,
  "budget": 500000.0
}
```

#### **📊 4. TEST BASIC CRUD OPERATIONS**
```bash
# Get all employees
GET http://localhost:8080/api/employees

# Get specific employee
GET http://localhost:8080/api/employees/1

# Update employee
PUT http://localhost:8080/api/employees/1
Content-Type: application/json

{
  "firstName": "Updated",
  "lastName": "Name",
  "jobTitle": "Senior Engineer"
}

# Delete employee
DELETE http://localhost:8080/api/employees/1
```

---

## 🗄️ **STEP 4: CHECK TABLE STATUS**

### **Check if Tables are Empty:**
```sql
-- Connect to your database and run:
USE employee_management_db;

-- Check table counts
SELECT 
  'employees' as table_name, COUNT(*) as record_count FROM employees
UNION ALL
SELECT 
  'departments' as table_name, COUNT(*) as record_count FROM departments
UNION ALL
SELECT 
  'attendance' as table_name, COUNT(*) as record_count FROM attendance
UNION ALL
SELECT 
  'tasks' as table_name, COUNT(*) as record_count FROM tasks;
```

### **Expected Results:**
- **Empty Database:** All counts = 0
- **With Mock Data:** Employees > 0, Departments > 0, etc.

---

## 🎯 **RECOMMENDED STARTING POINT**

### **If Tables are EMPTY:**
1. **Create Department First** (required for employees)
2. **Create Employee** (needs departmentId)
3. **Test Attendance** (needs employee)
4. **Test Tasks** (needs employee)
5. **Test Other Modules**

### **If Tables have MOCK DATA:**
1. **Test GET Operations** (list all employees)
2. **Test Search** (find employee by name)
3. **Test Updates** (modify existing data)
4. **Test Business Logic** (attendance, tasks, payroll)

---

## 🔧 **TESTING TOOLS**

### **Swagger UI (Recommended):**
```
http://localhost:8080/swagger-ui.html
```
- Interactive API documentation
- Test all endpoints directly
- No need for Postman/curl initially

### **Postman/Insomnia:**
- Import API collection
- Test complex workflows
- Save test scenarios

### **cURL Commands:**
```bash
# Quick health check
curl -X GET http://localhost:8080/actuator/health

# Test employee creation
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Test","lastName":"User","email":"test@company.com","jobTitle":"Engineer","departmentId":1,"salary":75000,"status":"active"}'
```

---

## 🚨 **COMMON STARTUP ISSUES**

### **Database Connection Failed:**
```properties
# Check these settings:
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_actual_password
```

### **Table Creation Issues:**
```properties
# Add this line to create tables:
spring.jpa.hibernate.ddl-auto=update
```

### **Port Conflicts:**
```properties
# Change port if 8080 is in use:
server.port=8081
```

---

## 📊 **TESTING CHECKLIST**

### **✅ Backend Startup:**
- [ ] Application starts without errors
- [ ] Database connection successful
- [ ] Tables created/verified
- [ ] Swagger UI accessible

### **✅ Basic CRUD:**
- [ ] Create employee works
- [ ] Get all employees works
- [ ] Get employee by ID works
- [ ] Update employee works
- [ ] Delete employee works

### **✅ Business Logic:**
- [ ] Department management works
- [ ] Attendance tracking works
- [ ] Task management works
- [ ] Search functionality works

---

## 🎯 **QUICK START COMMANDS**

### **1. Enable Database & Start:**
```bash
# Edit application.properties (uncomment database lines)
cd f:\Profolio_Projects\employee-backend
mvn spring-boot:run
```

### **2. Test in Browser:**
```
http://localhost:8080/swagger-ui.html
```

### **3. First API Test:**
```
POST /api/departments (create department)
POST /api/employees (create employee)
GET /api/employees (verify creation)
```

This will give you a complete starting point for testing all APIs! 🚀
