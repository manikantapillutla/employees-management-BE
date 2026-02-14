package com.ems.employee_backend.controller;


import com.ems.employee_backend.dto.EmployeeRequest;
import com.ems.employee_backend.dto.EmployeeResponse;
import com.ems.employee_backend.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping
    @Operation(summary = "Create a new employee", description = "Create a new employee record in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeRequest request) {
        return new ResponseEntity<>(employeeService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully")
    })
    public ResponseEntity<List<EmployeeResponse>> getAllEmployee() {
        return new ResponseEntity<>(employeeService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieve a specific employee by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search employees", description = "Search employees by name, email, or position")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(
            @Parameter(description = "Search query") @RequestParam String q) {
        return new ResponseEntity<>(employeeService.searchEmployees(q), HttpStatus.OK);
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Filter employees by department", description = "Get employees belonging to a specific department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully")
    })
    public ResponseEntity<List<EmployeeResponse>> filterByDepartment(
            @Parameter(description = "Department name") @PathVariable String department) {
        return new ResponseEntity<>(employeeService.filterByDepartment(department), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Filter employees by status", description = "Get employees by their status (Active/Inactive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully")
    })
    public ResponseEntity<List<EmployeeResponse>> filterByStatus(
            @Parameter(description = "Employee status") @PathVariable String status) {
        return new ResponseEntity<>(employeeService.filterByStatus(status), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee", description = "Update an existing employee's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id, 
            @RequestBody EmployeeRequest request) {
        return new ResponseEntity<>(employeeService.update(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee", description = "Delete an employee record from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        employeeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

