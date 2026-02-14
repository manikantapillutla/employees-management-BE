package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.PayrollRequest;
import com.ems.employee_backend.dto.PayrollResponse;
import com.ems.employee_backend.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping
    @Operation(summary = "Create payroll record", description = "Create a new payroll record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payroll record created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or duplicate payroll")
    })
    public ResponseEntity<PayrollResponse> createPayroll(@RequestBody PayrollRequest request) {
        return new ResponseEntity<>(payrollService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all payroll records", description = "Retrieve all payroll records")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payroll records retrieved successfully")
    })
    public ResponseEntity<List<PayrollResponse>> getAllPayroll() {
        return new ResponseEntity<>(payrollService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payroll by ID", description = "Retrieve a specific payroll record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payroll record retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Payroll record not found")
    })
    public ResponseEntity<PayrollResponse> getPayrollById(
            @Parameter(description = "Payroll ID") @PathVariable Long id) {
        return new ResponseEntity<>(payrollService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/period")
    @Operation(summary = "Get payroll by period", description = "Retrieve payroll records for a specific period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payroll records retrieved successfully")
    })
    public ResponseEntity<List<PayrollResponse>> getPayrollByPeriod(
            @Parameter(description = "Pay period") @RequestParam String period) {
        return new ResponseEntity<>(payrollService.getByPeriod(period), HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PayrollResponse>> getPayrollByEmployee(@PathVariable Long employeeId) {
        return new ResponseEntity<>(payrollService.getByEmployeeId(employeeId), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PayrollResponse>> getPayrollByStatus(@PathVariable String status) {
        return new ResponseEntity<>(payrollService.getByStatus(status), HttpStatus.OK);
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<PayrollResponse> processPayroll(@PathVariable Long id) {
        return new ResponseEntity<>(payrollService.process(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/mark-paid")
    public ResponseEntity<PayrollResponse> markPayrollAsPaid(@PathVariable Long id, @RequestParam LocalDate paymentDate) {
        return new ResponseEntity<>(payrollService.markAsPaid(id, paymentDate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
        payrollService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
