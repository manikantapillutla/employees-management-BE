package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.LeaveRequestRequest;
import com.ems.employee_backend.dto.LeaveRequestResponse;
import com.ems.employee_backend.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Leave Management", description = "APIs for managing leave requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    @Operation(summary = "Create leave request", description = "Create a new leave request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Leave request created successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<LeaveRequestResponse> createLeaveRequest(@RequestBody LeaveRequestRequest request) {
        return new ResponseEntity<>(leaveRequestService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all leave requests", description = "Retrieve all leave requests")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Leave requests retrieved successfully")
    })
    public ResponseEntity<List<LeaveRequestResponse>> getAllLeaveRequests() {
        return new ResponseEntity<>(leaveRequestService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get leave request by ID", description = "Retrieve a specific leave request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Leave request retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Leave request not found")
    })
    public ResponseEntity<LeaveRequestResponse> getLeaveRequestById(
            @Parameter(description = "Leave request ID") @PathVariable Long id) {
        return new ResponseEntity<>(leaveRequestService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get leave requests by employee", description = "Retrieve leave requests for a specific employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Leave requests retrieved successfully")
    })
    public ResponseEntity<List<LeaveRequestResponse>> getLeaveRequestsByEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        return new ResponseEntity<>(leaveRequestService.getByEmployeeId(employeeId), HttpStatus.OK);
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending leave requests", description = "Retrieve all pending leave requests")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pending leave requests retrieved successfully")
    })
    public ResponseEntity<List<LeaveRequestResponse>> getPendingLeaveRequests() {
        return new ResponseEntity<>(leaveRequestService.getByStatus("Pending"), HttpStatus.OK);
    }

    @GetMapping("/balance/{employeeId}")
    @Operation(summary = "Get leave balance for employee", description = "Retrieve leave balance for a specific employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Leave balance retrieved successfully")
    })
    public ResponseEntity<Object> getLeaveBalance(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId,
            @Parameter(description = "Year") @RequestParam(required = false) Integer year) {
        return new ResponseEntity<>(leaveRequestService.getLeaveBalance(employeeId, year), HttpStatus.OK);
    }

    @GetMapping("/report")
    @Operation(summary = "Generate leave report", description = "Generate leave report for date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Leave report generated successfully")
    })
    public ResponseEntity<Object> generateLeaveReport(
            @Parameter(description = "Start date") @RequestParam String startDate,
            @Parameter(description = "End date") @RequestParam String endDate,
            @Parameter(description = "Employee ID") @RequestParam(required = false) Long employeeId) {
        return new ResponseEntity<>(leaveRequestService.generateReport(startDate, endDate, employeeId), HttpStatus.OK);
    }

    @GetMapping("/calendar/{year}/{month}")
    @Operation(summary = "Get leave calendar", description = "Get leave calendar for specific month")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Leave calendar retrieved successfully")
    })
    public ResponseEntity<Object> getLeaveCalendar(
            @Parameter(description = "Year") @PathVariable int year,
            @Parameter(description = "Month") @PathVariable int month,
            @Parameter(description = "Employee ID") @RequestParam(required = false) Long employeeId) {
        return new ResponseEntity<>(leaveRequestService.getLeaveCalendar(year, month, employeeId), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveRequestResponse>> getLeaveRequestsByStatus(@PathVariable String status) {
        return new ResponseEntity<>(leaveRequestService.getByStatus(status), HttpStatus.OK);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequestResponse> approveLeaveRequest(@PathVariable Long id, @RequestParam Long approvedBy) {
        return new ResponseEntity<>(leaveRequestService.approve(id, approvedBy), HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveRequestResponse> rejectLeaveRequest(@PathVariable Long id, @RequestParam String comments) {
        return new ResponseEntity<>(leaveRequestService.reject(id, comments), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
