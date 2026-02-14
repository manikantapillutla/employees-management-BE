package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.PerformanceRequest;
import com.ems.employee_backend.dto.PerformanceResponse;
import com.ems.employee_backend.service.PerformanceService;
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
@RequestMapping("/api/performance")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Performance Management", description = "APIs for managing performance reviews")
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping
    public ResponseEntity<PerformanceResponse> createPerformance(@RequestBody PerformanceRequest request) {
        return new ResponseEntity<>(performanceService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all performance reviews", description = "Retrieve all performance reviews")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Performance reviews retrieved successfully")
    })
    public ResponseEntity<List<PerformanceResponse>> getAllPerformance() {
        return new ResponseEntity<>(performanceService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
//    @Operation(summary = "Get performance review by ID", description = "Retrieve a specific performance review")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Performance review retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Performance review not found")
    })
    public ResponseEntity<PerformanceResponse> getPerformanceById(
            @Parameter(description = "Performance review ID") @PathVariable Long id) {
        return new ResponseEntity<>(performanceService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/reviews/employee/{employeeId}")
//    @Operation(summary = "Get performance reviews by employee", description = "Retrieve performance reviews for a specific employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Performance reviews retrieved successfully")
    })
    public ResponseEntity<List<PerformanceResponse>> getPerformanceByEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        return new ResponseEntity<>(performanceService.getByEmployeeId(employeeId), HttpStatus.OK);
    }

//    @GetMapping("/employee/{employeeId}")
//    public ResponseEntity<List<PerformanceResponse>> getPerformanceByEmployee(@PathVariable Long employeeId) {
//        return new ResponseEntity<>(performanceService.getByEmployeeId(employeeId), HttpStatus.OK);
//    }

    @GetMapping("/period/{reviewPeriod}")
    public ResponseEntity<List<PerformanceResponse>> getPerformanceByPeriod(@PathVariable String reviewPeriod) {
        return new ResponseEntity<>(performanceService.getByReviewPeriod(reviewPeriod), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceResponse> updatePerformance(@PathVariable Long id, @RequestBody PerformanceRequest request) {
        return new ResponseEntity<>(performanceService.update(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long id) {
        performanceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
