package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PerformanceResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long reviewerId;
    private String reviewerName;
    private LocalDate reviewPeriodStart;
    private LocalDate reviewPeriodEnd;
    private Double overallRating;
    private Double technicalSkills;
    private Double communication;
    private Double teamwork;
    private Double leadership;
    private Double problemSolving;
    private Double timeManagement;
    private String comments;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
