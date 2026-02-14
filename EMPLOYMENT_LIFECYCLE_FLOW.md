# Employee Lifecycle Flow - Start to End

## 🚀 **EMPLOYMENT LIFECYCLE**

### **1. RECRUITMENT & HIRING PHASE**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Job Posting  │───▶│   Application   │───▶│   Interview     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
                                              ┌─────────────────┐
                                              │   Job Offer     │
                                              └─────────────────┘
                                                        │
                                                        ▼
                                              ┌─────────────────┐
                                              │   Acceptance   │
                                              └─────────────────┘
```

### **2. ONBOARDING PHASE**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Contract Sign  │───▶│  Documentation │───▶│  System Setup  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
                                              ┌─────────────────┐
                                              │  Orientation   │
                                              └─────────────────┘
                                                        │
                                                        ▼
                                              ┌─────────────────┐
│  Employee Created│───▶│  hire_date set  │───▶│  Status: active │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### **3. ACTIVE EMPLOYMENT PHASE**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Daily Work    │───▶│   Attendance    │───▶│   Task Mgmt     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
                                              ┌─────────────────┐
│  Performance    │───▶│  Training       │───▶│  Payroll        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
                                              ┌─────────────────┐
│  Leave Mgmt     │───▶│  Projects       │───▶│  Reviews        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### **4. STATUS TRANSITIONS**
```
┌─────────────────┐
│   ACTIVE       │─────────────────┐
│                │                 │
│ • Working      │                 ▼
│ • Productive   │        ┌─────────────────┐
│ • Engaged      │        │   ON_LEAVE     │
└─────────────────┘        │                │
         │                │ • Vacation     │
         │                │ • Sick Leave   │
         │                │ • Personal     │
         ▼                └─────────────────┘
┌─────────────────┐                 │
│   INACTIVE     │─────────────────┘
│                │
│ • Resigned     │
│ • Terminated   │
│ • Retired      │
└─────────────────┘
```

## 📊 **DATABASE FLOW MAPPING**

### **Employee Status Flow:**
```sql
-- Initial State
INSERT INTO employees (status = 'active', hire_date = CURRENT_DATE)

-- Status Changes
UPDATE employees SET status = 'on_leave' WHERE id = ?
UPDATE employees SET status = 'inactive' WHERE id = ?
```

### **Daily Operations Flow:**
```sql
-- 1. Attendance Tracking
INSERT INTO attendance (employee_id, date, status, check_in, check_out)

-- 2. Task Management
INSERT INTO tasks (assigned_to, status, due_date, created_by)

-- 3. Performance Tracking
INSERT INTO performance_reviews (employee_id, status, overall_rating)

-- 4. Payroll Processing
INSERT INTO payroll_records (employee_id, status, base_salary, net_pay)
```

## 🔄 **COMPLETE WORKFLOW EXAMPLE**

### **Day in the Life:**
```
1. Employee clocks IN → attendance.check_in = TIME('09:00')
2. Works on assigned tasks → tasks.status = 'in_progress'
3. Takes lunch break → attendance.break_duration = 60
4. Completes tasks → tasks.status = 'completed', tasks.progress = 100
5. Employee clocks OUT → attendance.check_out = TIME('17:00')
6. System calculates overtime → attendance.overtime_hours = 2.5
7. Monthly payroll processed → payroll.status = 'processed'
8. Performance review → performance.status = 'completed'
```

### **Leave Request Flow:**
```
1. Employee applies → leave_requests.status = 'pending'
2. Manager reviews → leave_requests.approved_by = manager_id
3. Decision made → leave_requests.status = 'approved'/'rejected'
4. If approved → employees.status = 'on_leave'
5. Leave period → attendance.status = 'leave'
6. Return to work → employees.status = 'active'
```

### **Training Development Flow:**
```
1. Training created → training_programs.status = 'planned'
2. Employee enrolls → training_enrollments.status = 'enrolled'
3. Training starts → training_programs.status = 'ongoing'
4. Employee attends → training_enrollments.status = 'in_progress'
5. Training completes → training_programs.status = 'completed'
6. Certification → training_enrollments.status = 'completed'
```

## 📈 **KEY TRANSITION POINTS**

### **Hire Date → Active Status:**
- `hire_date` set when employee is created
- `status = 'active'` by default
- Department assignment via `department_id`

### **Active → On Leave:**
- Leave request approved
- `employees.status = 'on_leave'`
- Daily attendance marked as 'leave'

### **On Leave → Active:**
- Leave period ends
- `employees.status = 'active'`
- Normal attendance tracking resumes

### **Active → Inactive:**
- Resignation, termination, or retirement
- `employees.status = 'inactive'`
- Final payroll processed
- Access revoked

## 🎯 **END-TO-END EMPLOYEE JOURNEY**

```
📅 Day 0:    hire_date = 2022-01-15, status = 'active'
📅 Daily:    attendance tracking, task management
📅 Monthly:   payroll processing, performance reviews
📅 Quarterly: training programs, skill development
📅 Yearly:    annual reviews, salary adjustments
📅 Exit:     status = 'inactive', final settlement
```

This flow covers the complete employee lifecycle from hiring to separation, with all intermediate states and transitions properly mapped to the database schema.
