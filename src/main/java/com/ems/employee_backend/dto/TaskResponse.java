package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Long projectId;
    private String projectName;
    private Long assignedTo;
    private String assignedToName;
    private Long createdBy;
    private String createdByName;
    private String priority;
    private String status;
    private LocalDate dueDate;
    private Double estimatedHours;
    private Double actualHours;
    private Integer progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
