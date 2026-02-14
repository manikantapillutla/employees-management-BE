package com.ems.employee_backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TrainingEnrollmentResponse {
    private Long id;
    private Long trainingId;
    private String trainingTitle;
    private Long employeeId;
    private String employeeName;
    private Long enrolledById;
    private String enrolledByName;
    private String status;
    private LocalDateTime enrolledAt;
    private LocalDate completedAt;
    private Double score;
    private String feedback;
    private Integer rating;
    private String certificateUrl;
    private LocalDateTime createdAt;
}
