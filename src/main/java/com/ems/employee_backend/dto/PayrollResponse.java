package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PayrollResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private Double baseSalary;
    private Double overtimeHours;
    private Double overtimeRate;
    private Double overtimePay;
    private Double bonuses;
    private Double deductions;
    private Double grossPay;
    private Double netPay;
    private Double taxes;
    private Double insurance;
    private Double retirement;
    private LocalDate paymentDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
