package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PayrollRequest {
    private Long employeeId;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private Double baseSalary;
    private Double overtimeHours;
    private Double overtimeRate;
    private Double overtimePay;
    private Double bonuses;
    private Double deductions;
    private Double taxes;
    private Double insurance;
    private Double retirement;
    private String status;
}
