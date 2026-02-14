package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.TaskRequest;
import com.ems.employee_backend.dto.TaskResponse;
import com.ems.employee_backend.service.TaskService;
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
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        return new ResponseEntity<>(taskService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<TaskResponse>> getTasksByEmployee(@PathVariable Long employeeId) {
        return new ResponseEntity<>(taskService.getByEmployeeId(employeeId), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable String status) {
        return new ResponseEntity<>(taskService.getByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks() {
        return new ResponseEntity<>(taskService.getOverdueTasks(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        return new ResponseEntity<>(taskService.update(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Delete a task from system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Task ID") @PathVariable Long id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update task status", description = "Update the status of a task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        return new ResponseEntity<>(taskService.updateStatus(id, request.get("status")), HttpStatus.OK);
    }

    @PatchMapping("/{id}/assign")
    @Operation(summary = "Assign task", description = "Assign a task to an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task assigned successfully"),
        @ApiResponse(responseCode = "404", description = "Task or employee not found")
    })
    public ResponseEntity<TaskResponse> assignTask(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @RequestBody Map<String, Long> request) {
        return new ResponseEntity<>(taskService.assignTask(id, request.get("assignedTo")), HttpStatus.OK);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get task statistics", description = "Get task completion statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task statistics retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getTaskStatistics(
            @Parameter(description = "Employee ID (optional)") @RequestParam(required = false) Long employeeId) {
        return new ResponseEntity<>(taskService.getTaskStatistics(employeeId), HttpStatus.OK);
    }
}
