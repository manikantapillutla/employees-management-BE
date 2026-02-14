package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.DepartmentRequest;
import com.ems.employee_backend.dto.DepartmentResponse;
import com.ems.employee_backend.service.DepartmentService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Department Management", description = "APIs for managing departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @Operation(summary = "Create a new department", description = "Create a new department in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Department created successfully"),
        @ApiResponse(responseCode = "400", description = "Department name already exists"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentRequest request){
        return new ResponseEntity<>(departmentService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all departments", description = "Retrieve a list of all departments with employee counts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Departments retrieved successfully")
    })
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments(){
        return new ResponseEntity<>(departmentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get department statistics", description = "Retrieve department and employee statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getDepartmentStats(){
        return new ResponseEntity<>(departmentService.getStats(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a department", description = "Update an existing department's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Department updated successfully"),
        @ApiResponse(responseCode = "404", description = "Department not found"),
        @ApiResponse(responseCode = "400", description = "Department name already exists")
    })
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @Parameter(description = "Department ID") @PathVariable Long id, 
            @RequestBody DepartmentRequest request){
        return new ResponseEntity<>(departmentService.update(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a department", description = "Delete a department from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Department deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Department not found"),
        @ApiResponse(responseCode = "400", description = "Cannot delete department with employees")
    })
    public ResponseEntity<Void> deleteDepartment(
            @Parameter(description = "Department ID") @PathVariable Long id){
        departmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
