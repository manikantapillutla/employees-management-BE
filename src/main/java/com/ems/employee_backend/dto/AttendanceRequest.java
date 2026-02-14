package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AttendanceRequest {
    private Long employeeId;
    private LocalDate date;
    private String checkIn;
    private String checkOut;
    private String status;
    private String notes;
}
