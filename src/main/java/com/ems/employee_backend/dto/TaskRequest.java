package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskRequest {
    private Long projectId;
    private Long assignedTo;
    private Long createdBy;
    private String title;
    private String description;
    private String priority;
    private String status;
    private LocalDate dueDate;
    private Double estimatedHours;
    private Double actualHours;
    private String notes;
    private Integer progress;
}
