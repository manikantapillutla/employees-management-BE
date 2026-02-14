package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.TaskRequest;
import com.ems.employee_backend.dto.TaskResponse;
import com.ems.employee_backend.entity.Employee;
import com.ems.employee_backend.entity.Project;
import com.ems.employee_backend.entity.Task;
import com.ems.employee_backend.repository.EmployeeRepository;
import com.ems.employee_backend.repository.ProjectRepository;
import com.ems.employee_backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    public TaskResponse create(TaskRequest request) {
        Employee assignedTo = employeeRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new RuntimeException("Assigned employee not found"));
        
        Employee createdBy = employeeRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Creator employee not found"));

        Project project = null;
        if (request.getProjectId() != null) {
            project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .project(project)
                .employee(assignedTo)
                .createdBy(createdBy)
                .status(request.getStatus() != null ? request.getStatus() : "not_started")
                .priority(request.getPriority() != null ? request.getPriority() : "medium")
                .dueDate(request.getDueDate())
                .estimatedHours(request.getEstimatedHours())
                .actualHours(request.getActualHours())
                .progress(request.getProgress() != null ? request.getProgress() : 0)
                .build();

        Task savedTask = taskRepository.save(task);
        return convertToResponse(savedTask);
    }

    public List<TaskResponse> getAll() {
        return taskRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return convertToResponse(task);
    }

    public List<TaskResponse> getByEmployeeId(Long employeeId) {
        return taskRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getOverdueTasks() {
        return taskRepository.findByDueDateBefore(LocalDate.now()).stream()
                .filter(task -> !"completed".equals(task.getStatus()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse update(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getEstimatedHours() != null) task.setEstimatedHours(request.getEstimatedHours());
        if (request.getActualHours() != null) task.setActualHours(request.getActualHours());
        if (request.getProgress() != null) task.setProgress(request.getProgress());

        if (request.getAssignedTo() != null) {
            Employee employee = employeeRepository.findById(request.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            task.setEmployee(employee);
        }

        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            task.setProject(project);
        }

        if ("Completed".equals(request.getStatus())) {
            task.setCompletedDate(LocalDate.now());
        }
        if (request.getNotes() != null) task.setNotes(request.getNotes());

        Task updatedTask = taskRepository.save(task);
        return convertToResponse(updatedTask);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    public TaskResponse updateStatus(Long id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);
        if ("completed".equals(status)) {
            task.setProgress(100);
        }

        Task updatedTask = taskRepository.save(task);
        return convertToResponse(updatedTask);
    }

    public TaskResponse assignTask(Long id, Long assignedTo) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Employee employee = employeeRepository.findById(assignedTo)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        task.setEmployee(employee);
        Task updatedTask = taskRepository.save(task);
        return convertToResponse(updatedTask);
    }

    public Map<String, Object> getTaskStatistics(Long employeeId) {
        List<Task> tasks;
        if (employeeId != null) {
            tasks = taskRepository.findByEmployeeId(employeeId);
        } else {
            tasks = taskRepository.findAll();
        }

        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(t -> "completed".equals(t.getStatus())).count();
        long inProgressTasks = tasks.stream().filter(t -> "in_progress".equals(t.getStatus())).count();
        long overdueTasks = taskRepository.findByDueDateBefore(LocalDate.now()).stream()
                .filter(t -> !"completed".equals(t.getStatus()))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTasks", totalTasks);
        stats.put("completedTasks", completedTasks);
        stats.put("inProgressTasks", inProgressTasks);
        stats.put("overdueTasks", overdueTasks);
        stats.put("completionRate", totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0);

        return stats;
    }

    private TaskResponse convertToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setEstimatedHours(task.getEstimatedHours());
        response.setActualHours(task.getActualHours());
        response.setProgress(task.getProgress());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        
        if (task.getProject() != null) {
            response.setProjectId(task.getProject().getId());
            response.setProjectName(task.getProject().getName());
        }
        
        if (task.getEmployee() != null) {
            response.setAssignedTo(task.getEmployee().getId());
            response.setAssignedToName(task.getEmployee().getFirstName() + " " + task.getEmployee().getLastName());
        }
        
        if (task.getCreatedBy() != null) {
            response.setCreatedBy(task.getCreatedBy().getId());
            response.setCreatedByName(task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName());
        }
        
        return response;
    }

    public List<TaskResponse> getByStatus(String status) {
        return taskRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}
