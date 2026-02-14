package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.AttendanceRequest;
import com.ems.employee_backend.dto.AttendanceResponse;
import com.ems.employee_backend.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Attendance Management", description = "APIs for managing employee attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "Create attendance record", description = "Create a new attendance record for an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Attendance record created successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<AttendanceResponse> createAttendance(@RequestBody AttendanceRequest request) {
        return new ResponseEntity<>(attendanceService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all attendance records", description = "Retrieve all attendance records in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendance records retrieved successfully")
    })
    public ResponseEntity<List<AttendanceResponse>> getAllAttendance() {
        return new ResponseEntity<>(attendanceService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get attendance by employee", description = "Retrieve attendance records for a specific employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendance records retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<List<AttendanceResponse>> getAttendanceByEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        return new ResponseEntity<>(attendanceService.getByEmployeeId(employeeId), HttpStatus.OK);
    }

    @GetMapping("/range")
    public ResponseEntity<List<AttendanceResponse>> getAttendanceByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return new ResponseEntity<>(attendanceService.getByDateRange(startDate, endDate), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponse> updateAttendance(@PathVariable Long id, @RequestBody AttendanceRequest request) {
        return new ResponseEntity<>(attendanceService.update(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete attendance record", description = "Delete an attendance record from system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Attendance record deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Attendance record not found")
    })
    public ResponseEntity<Void> deleteAttendance(
            @Parameter(description = "Attendance ID") @PathVariable Long id) {
        attendanceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/check-in")
    @Operation(summary = "Check in employee", description = "Record employee check-in time")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Check-in recorded successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<AttendanceResponse> checkIn(@RequestBody Map<String, Long> request) {
        return new ResponseEntity<>(attendanceService.checkIn(request.get("employeeId")), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/check-out")
    @Operation(summary = "Check out employee", description = "Record employee check-out time")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check-out recorded successfully"),
        @ApiResponse(responseCode = "404", description = "Attendance record not found")
    })
    public ResponseEntity<AttendanceResponse> checkOut(
            @Parameter(description = "Attendance ID") @PathVariable Long id) {
        return new ResponseEntity<>(attendanceService.checkOut(id), HttpStatus.OK);
    }

    @GetMapping("/summary/{employeeId}")
    @Operation(summary = "Get attendance summary", description = "Get attendance summary for an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendance summary retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getAttendanceSummary(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId,
            @Parameter(description = "Month") @RequestParam(required = false) String month) {
        return new ResponseEntity<>(attendanceService.getSummary(employeeId, month), HttpStatus.OK);
    }
}
