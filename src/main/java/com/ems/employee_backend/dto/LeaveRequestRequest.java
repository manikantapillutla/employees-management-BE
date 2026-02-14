package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveRequestRequest {
    private Long employeeId;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
