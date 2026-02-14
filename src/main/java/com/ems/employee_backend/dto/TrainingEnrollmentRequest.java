package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TrainingEnrollmentRequest {
    private Long trainingId;
    private Long employeeId;
    private Long enrolledBy;
}
