package com.ems.employee_backend.dto;



import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private String status;
    private LocalDate startDate;
    private Long departmentId;
}

