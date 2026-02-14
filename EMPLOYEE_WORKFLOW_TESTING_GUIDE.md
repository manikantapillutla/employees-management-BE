# 🧪 Employee Daily Workflow - End-to-End Testing Guide

## 🎯 **COMPLETE TESTING SCENARIO**

### **Employee Daily Workflow:**
```
Login → Check In → View Tasks → Work → Check Out → Notifications
```

---

## 🚀 **STEP-BY-STEP TESTING INSTRUCTIONS**

### **📍 STEP 1: LOGIN**
**Where to go:** `http://localhost:3000/login` (or main page if login is integrated)

**What to do:**
1. Open browser and navigate to the UI application
2. Enter employee credentials:
   - **Email:** `john.smith@company.com` (from mock data)
   - **Password:** `password123` (or whatever test password you've set)
3. Click "Login" button

**Expected Result:**
- Redirect to Dashboard page
- See employee name and role displayed
- Dashboard shows employee statistics

**Backend API Called:** `POST /api/auth/login`

---

### **📍 STEP 2: DASHBOARD OVERVIEW**
**Where to go:** `http://localhost:3000/dashboard`

**What to verify:**
1. Employee information displayed correctly
2. Quick action buttons visible:
   - Check In/Check Out
   - View Tasks
   - Notifications
3. Today's attendance status
4. Pending tasks count

**Expected Result:**
- Dashboard loads with employee data
- Quick access to daily functions

**Backend APIs Called:**
- `GET /api/employees/me` (current employee info)
- `GET /api/attendance/today` (today's attendance)
- `GET /api/tasks/employee/{id}/pending` (pending tasks)

---

### **📍 STEP 3: CHECK IN**
**Where to go:** Click "Check In" button on Dashboard OR navigate to `http://localhost:3000/attendance`

**What to do:**
1. Click the "Check In" button
2. Confirm check-in time
3. System should record current time

**Expected Result:**
- Check-in button becomes "Check Out"
- Success message displayed
- Attendance record created

**Backend API Called:** `POST /api/attendance/checkin`
```json
Request: {
  "employeeId": 1,
  "timestamp": "2024-02-15T09:00:00"
}
Response: {
  "success": true,
  "attendanceId": 123,
  "checkInTime": "09:00"
}
```

---

### **📍 STEP 4: VIEW TASKS**
**Where to go:** Click "Tasks" in sidebar OR navigate to `http://localhost:3000/tasks`

**What to do:**
1. View assigned tasks list
2. Click on a task to see details
3. Check task status and progress

**Expected Result:**
- List of employee's assigned tasks
- Task details visible (title, description, due date, status)
- Ability to update task status

**Backend API Called:** `GET /api/tasks/employee/{id}`

**Sample Tasks from Mock Data:**
```json
[
  {
    "id": 1,
    "title": "Fix login authentication bug",
    "status": "in_progress",
    "dueDate": "2024-02-15",
    "progress": 62
  },
  {
    "id": 8,
    "title": "Review code changes",
    "status": "completed",
    "progress": 100
  }
]
```

---

### **📍 STEP 5: WORK ON TASKS**
**Where to go:** Stay on Tasks page OR click on specific task

**What to do:**
1. Select a task with "in_progress" status
2. Update task progress (e.g., from 62% to 80%)
3. Add notes/comments if needed
4. Click "Update Task"

**Expected Result:**
- Task progress updates successfully
- Status changes if progress reaches 100%
- Task moves to "completed" if finished

**Backend API Called:** `PUT /api/tasks/{id}`
```json
Request: {
  "progress": 80,
  "notes": "Fixed authentication logic, testing required"
}
Response: {
  "success": true,
  "task": {
    "id": 1,
    "progress": 80,
    "status": "in_progress"
  }
}
```

---

### **📍 STEP 6: CHECK OUT**
**Where to go:** Return to Dashboard OR stay on Attendance page

**What to do:**
1. Click "Check Out" button
2. Confirm check-out time
3. System calculates overtime if applicable

**Expected Result:**
- Check-out completed
- Overtime hours calculated (if any)
- Attendance record updated

**Backend API Called:** `POST /api/attendance/checkout`
```json
Request: {
  "attendanceId": 123,
  "timestamp": "2024-02-15T17:30:00"
}
Response: {
  "success": true,
  "checkOutTime": "17:30",
  "overtimeHours": 2.5,
  "totalHours": 8.5
}
```

---

### **📍 STEP 7: NOTIFICATIONS**
**Where to go:** Click "Notifications" icon OR navigate to `http://localhost:3000/notifications`

**What to do:**
1. View today's notifications
2. Check for:
   - Task assignment notifications
   - Attendance confirmations
   - System alerts
3. Mark notifications as read

**Expected Result:**
- List of recent notifications
- Ability to mark as read/unread
- Notification badges update

**Backend API Called:** `GET /api/notifications`

**Sample Notifications:**
```json
[
  {
    "id": 1,
    "title": "Check-in Successful",
    "message": "You have successfully checked in at 09:00 AM",
    "type": "success",
    "read": false
  },
  {
    "id": 2,
    "title": "Task Assigned",
    "message": "New task: 'Fix login authentication bug' assigned to you",
    "type": "info",
    "read": false
  }
]
```

---

## 🔧 **TESTING SETUP REQUIREMENTS**

### **Prerequisites:**
1. **Backend Running:** `http://localhost:8080`
2. **Frontend Running:** `http://localhost:3000`
3. **Database:** MySQL with mock data imported
4. **Test Employee:** Use employee ID 1 (John Smith)

### **Start Backend:**
```bash
cd f:\Profolio_Projects\employee-backend
mvn spring-boot:run
```

### **Start Frontend:**
```bash
cd f:\Profolio_Projects\employee-management-ui
npm start
```

### **Import Mock Data:**
```sql
-- Import all mock data files
source f:\Profolio_Projects\employee-management-ui\mock-data\complete_schema.sql;
source f:\Profolio_Projects\employee-management-ui\mock-data\employees.sql;
source f:\Profolio_Projects\employee-management-ui\mock-data\attendance.sql;
source f:\Profolio_Projects\employee-management-ui\mock-data\task_management.sql;
```

---

## 🐛 **TROUBLESHOOTING**

### **Common Issues:**

**1. Login Fails:**
- Check if backend is running on port 8080
- Verify database connection in `application.properties`
- Check if employee exists in database

**2. Check-in/Check-out Not Working:**
- Verify AttendanceController endpoints
- Check if attendance records are being created
- Review console errors in browser

**3. Tasks Not Loading:**
- Verify TaskController endpoints
- Check if task data exists in database
- Review API service calls in browser network tab

**4. Notifications Not Showing:**
- Verify NotificationController endpoints
- Check if notification data exists
- Review WebSocket connection if using real-time updates

### **Debug Tips:**
1. **Browser Console:** Check for JavaScript errors
2. **Network Tab:** Monitor API calls and responses
3. **Backend Logs:** Check Spring Boot console for errors
4. **Database:** Verify data exists in tables

---

## 📊 **TESTING CHECKLIST**

### **✅ Login Test:**
- [ ] Can login with valid credentials
- [ ] Redirects to dashboard successfully
- [ ] Employee info displayed correctly

### **✅ Dashboard Test:**
- [ ] Dashboard loads employee data
- [ ] Quick action buttons work
- [ ] Today's attendance status shown

### **✅ Check-in Test:**
- [ ] Check-in button works
- [ ] Attendance record created
- [ ] Button changes to "Check Out"

### **✅ Tasks Test:**
- [ ] Tasks list loads
- [ ] Can view task details
- [ ] Can update task progress
- [ ] Status changes correctly

### **✅ Check-out Test:**
- [ ] Check-out button works
- [ ] Overtime calculated
- [ ] Attendance record updated

### **✅ Notifications Test:**
- [ ] Notifications load
- [ ] Can mark as read/unread
- [ ] Real-time updates work (if implemented)

---

## 🎯 **SUCCESS CRITERIA**

**Complete Success:**
- All steps work without errors
- Data flows correctly between UI and backend
- Database records are created/updated properly
- User experience is smooth and intuitive

**Partial Success:**
- Some steps work but others have issues
- Backend APIs work but UI has problems
- Database operations work but display issues exist

**Next Steps:**
- Fix any failing components
- Improve error handling
- Add more test cases
- Implement missing features

This testing guide will help you verify the complete employee daily workflow from login to notifications! 🚀
