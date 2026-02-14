package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.TrainingEnrollmentRequest;
import com.ems.employee_backend.dto.TrainingEnrollmentResponse;
import com.ems.employee_backend.dto.TrainingRequest;
import com.ems.employee_backend.dto.TrainingResponse;
import com.ems.employee_backend.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Training Management", description = "APIs for managing training programs")
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping
    @Operation(summary = "Create training", description = "Create a new training program")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Training created successfully")
    })
    public ResponseEntity<TrainingResponse> createTraining(@RequestBody TrainingRequest request) {
        return new ResponseEntity<>(trainingService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all trainings", description = "Retrieve all training programs")
    public ResponseEntity<List<TrainingResponse>> getAllTrainings() {
        return new ResponseEntity<>(trainingService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get training by ID", description = "Retrieve a specific training program")
    public ResponseEntity<TrainingResponse> getTrainingById(@PathVariable Long id) {
        return new ResponseEntity<>(trainingService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming trainings", description = "Retrieve upcoming training programs")
    public ResponseEntity<List<TrainingResponse>> getUpcomingTrainings() {
        return new ResponseEntity<>(trainingService.getUpcomingTrainings(), HttpStatus.OK);
    }

    @GetMapping("/available/{employeeId}")
    @Operation(summary = "Get available trainings", description = "Retrieve available training programs for employee")
    public ResponseEntity<List<TrainingResponse>> getAvailableTrainings(@PathVariable Long employeeId) {
        return new ResponseEntity<>(trainingService.getAvailableTrainings(), HttpStatus.OK);
    }

    @GetMapping("/calendar")
    @Operation(summary = "Get training calendar", description = "Retrieve training calendar")
    public ResponseEntity<List<TrainingResponse>> getTrainingCalendar(
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return new ResponseEntity<>(trainingService.getTrainingCalendar(startDate, endDate), HttpStatus.OK);
    }

    @PostMapping("/enrollments")
    @Operation(summary = "Enroll in training", description = "Enroll an employee in a training program")
    public ResponseEntity<TrainingEnrollmentResponse> enroll(@RequestBody TrainingEnrollmentRequest request) {
        return new ResponseEntity<>(trainingService.enroll(request.getTrainingId(), request.getEmployeeId(), request.getEnrolledBy()), HttpStatus.CREATED);
    }

    @GetMapping("/enrollments")
    @Operation(summary = "Get enrollments", description = "Retrieve training enrollments")
    public ResponseEntity<List<TrainingEnrollmentResponse>> getEnrollments(
            @RequestParam(required = false) Long trainingId,
            @RequestParam(required = false) Long employeeId) {
        return new ResponseEntity<>(trainingService.getEnrollments(trainingId, employeeId), HttpStatus.OK);
    }
}
