package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.AttendanceRequest;
import com.ems.employee_backend.dto.AttendanceResponse;
import com.ems.employee_backend.entity.Attendance;
import com.ems.employee_backend.entity.Employee;
import com.ems.employee_backend.repository.AttendanceRepository;
import com.ems.employee_backend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceResponse create(AttendanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .status(request.getStatus())
                .notes(request.getNotes())
                .build();

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToResponse(savedAttendance);
    }

    public List<AttendanceResponse> getAll() {
        return attendanceRepository.findAll().stream()
                .map(this::convertToResponse)
//                .sorted()
                .collect(Collectors.toList());
    }

    public List<AttendanceResponse> getByEmployeeId(Long employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponse> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateBetween(startDate, endDate).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public AttendanceResponse update(Long id, AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));

        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            attendance.setEmployee(employee);
        }

        if (request.getDate() != null) attendance.setDate(request.getDate());
        if (request.getCheckIn() != null) attendance.setCheckIn(request.getCheckIn());
        if (request.getCheckOut() != null) attendance.setCheckOut(request.getCheckOut());
        if (request.getStatus() != null) attendance.setStatus(request.getStatus());
        if (request.getNotes() != null) attendance.setNotes(request.getNotes());

        Attendance updatedAttendance = attendanceRepository.save(attendance);
        return convertToResponse(updatedAttendance);
    }

    public void delete(Long id) {
        if (!attendanceRepository.existsById(id)) {
            throw new RuntimeException("Attendance record not found");
        }
        attendanceRepository.deleteById(id);
    }

    public AttendanceResponse checkIn(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .date(LocalDate.now())
                .checkIn(java.time.LocalTime.now().toString())
                .status("Present")
                .build();

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToResponse(savedAttendance);
    }

    public AttendanceResponse checkOut(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));

        attendance.setCheckOut(java.time.LocalTime.now().toString());
        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToResponse(savedAttendance);
    }

    public Map<String, Object> getSummary(Long employeeId, String month) {
        List<Attendance> attendances = attendanceRepository.findByEmployeeId(employeeId);
        
        long totalDays = attendances.size();
        long presentDays = attendances.stream().filter(a -> "Present".equals(a.getStatus())).count();
        long absentDays = attendances.stream().filter(a -> "Absent".equals(a.getStatus())).count();
        long lateDays = attendances.stream().filter(a -> "Late".equals(a.getStatus())).count();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalDays", totalDays);
        summary.put("presentDays", presentDays);
        summary.put("absentDays", absentDays);
        summary.put("lateDays", lateDays);
        summary.put("attendanceRate", totalDays > 0 ? (presentDays * 100.0 / totalDays) : 0);
        
        return summary;
    }

    public List<Map<String, Object>> getMonthlySummary(String month) {
        List<Attendance> allAttendances = attendanceRepository.findAll();
        
        // Filter by month if provided
        List<Attendance> filteredAttendances = allAttendances.stream()
                .filter(a -> a.getDate().toString().startsWith(month))
                .collect(Collectors.toList());
        
        // Group by employee
        Map<Long, List<Attendance>> groupedByEmployee = filteredAttendances.stream()
                .collect(Collectors.groupingBy(a -> a.getEmployee().getId()));
        
        List<Map<String, Object>> summaries = new ArrayList<>();
        
        for (Map.Entry<Long, List<Attendance>> entry : groupedByEmployee.entrySet()) {
            Long employeeId = entry.getKey();
            List<Attendance> employeeAttendances = entry.getValue();
            
            long presentDays = employeeAttendances.stream().filter(a -> "Present".equals(a.getStatus())).count();
            long absentDays = employeeAttendances.stream().filter(a -> "Absent".equals(a.getStatus())).count();
            long lateDays = employeeAttendances.stream().filter(a -> "Late".equals(a.getStatus())).count();
            long halfDays = employeeAttendances.stream().filter(a -> "Half-day".equals(a.getStatus())).count();
            
            double totalOvertimeHours = employeeAttendances.stream()
                    .mapToDouble(a -> a.getOvertimeHours() != null ? a.getOvertimeHours() : 0.0)
                    .sum();
            
            double averageHoursPerDay = employeeAttendances.stream()
                    .mapToDouble(a -> calculateHoursWorked(a.getCheckIn(), a.getCheckOut()))
                    .average()
                    .orElse(0.0);
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("employeeId", employeeId);
            summary.put("month", month);
            summary.put("totalDays", employeeAttendances.size());
            summary.put("presentDays", presentDays);
            summary.put("absentDays", absentDays);
            summary.put("lateDays", lateDays);
            summary.put("halfDays", halfDays);
            summary.put("totalOvertimeHours", totalOvertimeHours);
            summary.put("averageHoursPerDay", averageHoursPerDay);
            
            summaries.add(summary);
        }
        
        return summaries;
    }

    public AttendanceResponse getById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
        return convertToResponse(attendance);
    }

    public Map<String, Object> generateReport(LocalDate startDate, LocalDate endDate, Long employeeId) {
        List<Attendance> attendances;
        
        if (employeeId != null) {
            attendances = attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
        } else {
            attendances = attendanceRepository.findByDateBetween(startDate, endDate);
        }
        
        // Calculate statistics
        long totalDays = attendances.size();
        long presentDays = attendances.stream().filter(a -> "Present".equals(a.getStatus())).count();
        long absentDays = attendances.stream().filter(a -> "Absent".equals(a.getStatus())).count();
        long lateDays = attendances.stream().filter(a -> "Late".equals(a.getStatus())).count();
        long halfDays = attendances.stream().filter(a -> "Half-day".equals(a.getStatus())).count();
        
        double totalHours = attendances.stream()
                .mapToDouble(a -> calculateHoursWorked(a.getCheckIn(), a.getCheckOut()))
                .sum();
        
        double totalOvertimeHours = attendances.stream()
                .mapToDouble(a -> a.getOvertimeHours() != null ? a.getOvertimeHours() : 0.0)
                .sum();
        
        // Group by employee for detailed breakdown
        Map<Long, List<Attendance>> byEmployee = attendances.stream()
                .collect(Collectors.groupingBy(a -> a.getEmployee().getId()));
        
        List<Map<String, Object>> employeeBreakdown = new ArrayList<>();
        for (Map.Entry<Long, List<Attendance>> entry : byEmployee.entrySet()) {
            Long empId = entry.getKey();
            List<Attendance> empAttendances = entry.getValue();
            Employee emp = empAttendances.get(0).getEmployee();
            
            long empPresent = empAttendances.stream().filter(a -> "Present".equals(a.getStatus())).count();
            long empAbsent = empAttendances.stream().filter(a -> "Absent".equals(a.getStatus())).count();
            long empLate = empAttendances.stream().filter(a -> "Late".equals(a.getStatus())).count();
            
            Map<String, Object> empData = new HashMap<>();
            empData.put("employeeId", empId);
            empData.put("employeeName", emp.getFirstName() + " " + emp.getLastName());
            empData.put("totalDays", empAttendances.size());
            empData.put("presentDays", empPresent);
            empData.put("absentDays", empAbsent);
            empData.put("lateDays", empLate);
            empData.put("attendanceRate", empAttendances.size() > 0 ? (empPresent * 100.0 / empAttendances.size()) : 0);
            employeeBreakdown.add(empData);
        }
        
        Map<String, Object> report = new HashMap<>();
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalEmployees", byEmployee.size());
        report.put("totalDays", totalDays);
        report.put("presentDays", presentDays);
        report.put("absentDays", absentDays);
        report.put("lateDays", lateDays);
        report.put("halfDays", halfDays);
        report.put("totalHours", totalHours);
        report.put("totalOvertimeHours", totalOvertimeHours);
        report.put("attendanceRate", totalDays > 0 ? (presentDays * 100.0 / totalDays) : 0);
        report.put("employeeBreakdown", employeeBreakdown);
        
        return report;
    }

    public Map<String, Object> getAttendanceCalendar(int year, int month, Long employeeId) {
        try {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

            List<Attendance> attendances;
            if (employeeId != null) {
                attendances = attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
            } else {
                attendances = attendanceRepository.findByDateBetween(startDate, endDate);
            }

            // Create calendar data structure
            Map<Integer, Map<String, Object>> calendarDays = new HashMap<>();

            // Initialize all days of month
            for (int day = 1; day <= endDate.getDayOfMonth(); day++) {
                LocalDate currentDate = LocalDate.of(year, month, day);
                DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", currentDate);
                dayData.put("isWeekend", dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
                dayData.put("attendances", new ArrayList<>());

                // Use mutable HashMap for summary
                Map<String, Integer> summary = new HashMap<>();
                summary.put("present", 0);
                summary.put("absent", 0);
                summary.put("late", 0);
                summary.put("halfDay", 0);
                dayData.put("summary", summary);

                calendarDays.put(day, dayData);
            }

            // Populate with attendance data
            for (Attendance attendance : attendances) {
                int day = attendance.getDate().getDayOfMonth();
                Map<String, Object> dayData = calendarDays.get(day);

                if (dayData != null) {
                    List<Attendance> dayAttendances = (List<Attendance>) dayData.get("attendances");
                    dayAttendances.add(attendance);

                    // Update summary
                    Map<String, Integer> summary = (Map<String, Integer>) dayData.get("summary");
                    String status = attendance.getStatus();
                    switch (status) {
                        case "Present" -> summary.put("present", summary.get("present") + 1);
                        case "Absent" -> summary.put("absent", summary.get("absent") + 1);
                        case "Late" -> summary.put("late", summary.get("late") + 1);
                        case "Half-day" -> summary.put("halfDay", summary.get("halfDay") + 1);
                    }
                }
            }

            Map<String, Object> calendar = new HashMap<>();
            calendar.put("year", year);
            calendar.put("month", month);
            calendar.put("monthName", startDate.getMonth().toString());
            calendar.put("startDate", startDate);
            calendar.put("endDate", endDate);
            calendar.put("days", calendarDays);
            calendar.put("totalAttendances", attendances.size());

            return calendar;
        } catch (Exception e) {
            throw new RuntimeException("Error generating calendar: " + e.getMessage(), e);
        }
    }
    private double calculateHoursWorked(String checkIn, String checkOut) {
        if (checkIn == null || checkOut == null) return 0.0;
        
        try {
            String[] checkInParts = checkIn.split(":");
            String[] checkOutParts = checkOut.split(":");
            
            double checkInHours = Double.parseDouble(checkInParts[0]) + Double.parseDouble(checkInParts[1]) / 60.0;
            double checkOutHours = Double.parseDouble(checkOutParts[0]) + Double.parseDouble(checkOutParts[1]) / 60.0;
            
            return Math.max(0, checkOutHours - checkInHours);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private AttendanceResponse convertToResponse(Attendance attendance) {
        AttendanceResponse response = new AttendanceResponse();
        response.setId(attendance.getId());
        response.setEmployeeId(attendance.getEmployee().getId());
        response.setEmployeeName(attendance.getEmployee().getFirstName() + " " + attendance.getEmployee().getLastName());
        response.setDate(attendance.getDate());
        response.setCheckIn(attendance.getCheckIn());
        response.setCheckOut(attendance.getCheckOut());
        response.setBreakDuration(attendance.getBreakDuration());
        response.setStatus(attendance.getStatus());
        response.setOvertimeHours(attendance.getOvertimeHours());
        response.setNotes(attendance.getNotes());
        response.setCreatedAt(attendance.getCreatedAt());
        response.setUpdatedAt(attendance.getUpdatedAt());
        return response;
    }
}
