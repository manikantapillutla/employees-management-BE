package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate date;
    private String checkIn;
    private String checkOut;
    private Integer breakDuration;
    private String status;
    private Double overtimeHours;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
