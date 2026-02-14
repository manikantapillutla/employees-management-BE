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
                .sorted()
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
