package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.LeaveRequestRequest;
import com.ems.employee_backend.dto.LeaveRequestResponse;
import com.ems.employee_backend.entity.Employee;
import com.ems.employee_backend.entity.LeaveRequest;
import com.ems.employee_backend.repository.EmployeeRepository;
import com.ems.employee_backend.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestResponse create(LeaveRequestRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .employee(employee)
                .leaveType(request.getLeaveType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status("pending")
                .build();

        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);
        return convertToResponse(savedLeaveRequest);
    }

    public List<LeaveRequestResponse> getAll() {
        return leaveRequestRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public LeaveRequestResponse getById(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        return convertToResponse(leaveRequest);
    }

    public List<LeaveRequestResponse> getByEmployeeId(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestResponse> getByStatus(String status) {
        return leaveRequestRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public LeaveRequestResponse approve(Long id, Long approvedById) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        Employee approvedBy = employeeRepository.findById(approvedById)
                .orElseThrow(() -> new RuntimeException("Approving employee not found"));

        leaveRequest.setStatus("approved");
        leaveRequest.setApprovedBy(approvedBy);

        LeaveRequest updatedLeaveRequest = leaveRequestRepository.save(leaveRequest);
        return convertToResponse(updatedLeaveRequest);
    }

    public LeaveRequestResponse reject(Long id, String comments) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        leaveRequest.setStatus("rejected");
        leaveRequest.setRejectionReason(comments);

        LeaveRequest updatedLeaveRequest = leaveRequestRepository.save(leaveRequest);
        return convertToResponse(updatedLeaveRequest);
    }

    public void delete(Long id) {
        if (!leaveRequestRepository.existsById(id)) {
            throw new RuntimeException("Leave request not found");
        }
        leaveRequestRepository.deleteById(id);
    }

    public Map<String, Object> getLeaveBalance(Long employeeId, Integer year) {
        int currentYear = year != null ? year : LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(currentYear, 1, 1);
        LocalDate endDate = LocalDate.of(currentYear, 12, 31);
        
        List<LeaveRequest> leaveRequests = leaveRequestRepository
                .findByEmployeeIdAndStartDateBetween(employeeId, startDate, endDate);
        
        Map<String, Object> balance = new HashMap<>();
        Map<String, Object> annualLeave = new HashMap<>();
        Map<String, Object> sickLeave = new HashMap<>();
        Map<String, Object> personalLeave = new HashMap<>();
        Map<String, Object> maternityLeave = new HashMap<>();
        Map<String, Object> paternityLeave = new HashMap<>();
        
        // Default totals
        annualLeave.put("total", 21);
        sickLeave.put("total", 10);
        personalLeave.put("total", 7);
        maternityLeave.put("total", 90);
        paternityLeave.put("total", 15);
        
        // Calculate used days
        int annualUsed = 0, sickUsed = 0, personalUsed = 0, maternityUsed = 0, paternityUsed = 0;
        
        for (LeaveRequest leave : leaveRequests) {
            if (leave.getStatus().equals("approved")) {
                int days = calculateDays(leave.getStartDate(), leave.getEndDate());
                switch (leave.getLeaveType()) {
                    case "annual" -> annualUsed += days;
                    case "sick" -> sickUsed += days;
                    case "personal" -> personalUsed += days;
                    case "maternity" -> maternityUsed += days;
                    case "paternity" -> paternityUsed += days;
                }
            }
        }
        
        annualLeave.put("used", annualUsed);
        sickLeave.put("used", sickUsed);
        personalLeave.put("used", personalUsed);
        maternityLeave.put("used", maternityUsed);
        paternityLeave.put("used", paternityUsed);
        
        balance.put("employeeId", employeeId);
        balance.put("year", currentYear);
        balance.put("annualLeave", annualLeave);
        balance.put("sickLeave", sickLeave);
        balance.put("personalLeave", personalLeave);
        balance.put("maternityLeave", maternityLeave);
        balance.put("paternityLeave", paternityLeave);
        
        return balance;
    }

    public Map<String, Object> generateReport(String startDate, String endDate, Long employeeId) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        
        List<LeaveRequest> leaveRequests;
        if (employeeId != null) {
            leaveRequests = leaveRequestRepository.findByEmployeeIdAndStartDateBetween(employeeId, start, end);
        } else {
            leaveRequests = leaveRequestRepository.findByStartDateBetween(start, end);
        }
        
        Map<String, Object> report = new HashMap<>();
        List<Map<String, Object>> employeeBreakdown = new ArrayList<>();
        
        Map<String, Integer> totals = new HashMap<>();
        totals.put("totalRequests", leaveRequests.size());
        totals.put("approved", (int) leaveRequests.stream().filter(l -> l.getStatus().equals("approved")).count());
        totals.put("pending", (int) leaveRequests.stream().filter(l -> l.getStatus().equals("pending")).count());
        totals.put("rejected", (int) leaveRequests.stream().filter(l -> l.getStatus().equals("rejected")).count());
        
        // Group by employee
        Map<Long, List<LeaveRequest>> byEmployee = leaveRequests.stream()
                .collect(Collectors.groupingBy(l -> l.getEmployee().getId()));
        
        for (Map.Entry<Long, List<LeaveRequest>> entry : byEmployee.entrySet()) {
            Employee emp = entry.getValue().get(0).getEmployee();
            Map<String, Object> empData = new HashMap<>();
            empData.put("employeeId", emp.getId());
            empData.put("employeeName", emp.getFirstName() + " " + emp.getLastName());
            empData.put("totalRequests", entry.getValue().size());
            empData.put("approved", (int) entry.getValue().stream().filter(l -> l.getStatus().equals("approved")).count());
            empData.put("pending", (int) entry.getValue().stream().filter(l -> l.getStatus().equals("pending")).count());
            empData.put("rejected", (int) entry.getValue().stream().filter(l -> l.getStatus().equals("rejected")).count());
            
            int totalDays = entry.getValue().stream()
                    .filter(l -> l.getStatus().equals("approved"))
                    .mapToInt(l -> calculateDays(l.getStartDate(), l.getEndDate()))
                    .sum();
            empData.put("totalDays", totalDays);
            
            employeeBreakdown.add(empData);
        }
        
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totals", totals);
        report.put("employeeBreakdown", employeeBreakdown);
        
        return report;
    }

    public Map<String, Object> getLeaveCalendar(int year, int month, Long employeeId) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        List<LeaveRequest> leaveRequests;
        if (employeeId != null) {
            leaveRequests = leaveRequestRepository.findByEmployeeIdAndStartDateBetween(employeeId, startDate, endDate);
        } else {
            leaveRequests = leaveRequestRepository.findByStartDateBetween(startDate, endDate);
        }
        
        Map<Integer, Map<String, Object>> calendarDays = new HashMap<>();
        
        // Initialize all days of month
        for (int day = 1; day <= endDate.getDayOfMonth(); day++) {
            LocalDate currentDate = LocalDate.of(year, month, day);
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", currentDate);
            dayData.put("isWeekend", dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
            dayData.put("leaves", new ArrayList<>());
            dayData.put("summary", new HashMap<String, Integer>() {{
                put("annual", 0);
                put("sick", 0);
                put("personal", 0);
                put("maternity", 0);
                put("paternity", 0);
            }});
            calendarDays.put(day, dayData);
        }
        
        // Populate with leave data
        for (LeaveRequest leave : leaveRequests) {
            if (leave.getStatus().equals("approved")) {
                LocalDate start = leave.getStartDate();
                LocalDate end = leave.getEndDate();
                
                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    if (date.getMonthValue() == month && date.getYear() == year) {
                        int day = date.getDayOfMonth();
                        Map<String, Object> dayData = calendarDays.get(day);
                        
                        if (dayData != null) {
                            List<LeaveRequest> dayLeaves = (List<LeaveRequest>) dayData.get("leaves");
                            dayLeaves.add(leave);
                            
                            Map<String, Integer> summary = (Map<String, Integer>) dayData.get("summary");
                            String type = leave.getLeaveType();
                            summary.put(type, summary.get(type) + 1);
                        }
                    }
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
        calendar.put("totalLeaves", leaveRequests.size());
        
        return calendar;
    }

    private LeaveRequestResponse convertToResponse(LeaveRequest leaveRequest) {
        LeaveRequestResponse response = new LeaveRequestResponse();
        response.setId(leaveRequest.getId());
        response.setEmployeeId(leaveRequest.getEmployee().getId());
        response.setEmployeeName(leaveRequest.getEmployee().getFirstName() + " " + leaveRequest.getEmployee().getLastName());
        response.setLeaveType(leaveRequest.getLeaveType());
        response.setStartDate(leaveRequest.getStartDate());
        response.setEndDate(leaveRequest.getEndDate());
        response.setReason(leaveRequest.getReason());
        response.setStatus(leaveRequest.getStatus());
        response.setApprovedById(leaveRequest.getApprovedBy() != null ? leaveRequest.getApprovedBy().getId() : null);
        response.setApprovedByName(leaveRequest.getApprovedBy() != null ? 
                leaveRequest.getApprovedBy().getFirstName() + " " + leaveRequest.getApprovedBy().getLastName() : null);
        response.setApprovedAt(leaveRequest.getApprovedAt());
        response.setCreatedAt(leaveRequest.getCreatedAt());
        response.setUpdatedAt(leaveRequest.getUpdatedAt());
        response.setDays(calculateDays(leaveRequest.getStartDate(), leaveRequest.getEndDate()));
        return response;
    }

    private int calculateDays(LocalDate startDate, LocalDate endDate) {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
