package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PerformanceRequest {
    private Long employeeId;
    private Long reviewerId;
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
}
