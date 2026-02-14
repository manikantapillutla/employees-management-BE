package com.ems.employee_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll_records")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(name = "pay_period_start")
    private LocalDate payPeriodStart;
    
    @Column(name = "pay_period_end")
    private LocalDate payPeriodEnd;
    
    @Column(name = "base_salary")
    private Double baseSalary;
    
    @Column(name = "overtime_hours")
    private Double overtimeHours;
    
    @Column(name = "overtime_rate")
    private Double overtimeRate;
    
    @Column(name = "overtime_pay")
    private Double overtimePay;
    
    private Double bonuses;
    private Double deductions;
    @Column(name = "gross_pay")
    private Double grossPay;
    @Column(name = "net_pay")
    private Double netPay;
    private Double taxes;
    private Double insurance;
    private Double retirement;
    private String status; // pending, processed, paid
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "pending";
        }
    }
    
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
