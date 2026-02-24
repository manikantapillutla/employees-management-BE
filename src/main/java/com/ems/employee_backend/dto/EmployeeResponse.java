package com.ems.employee_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private Long departmentId;
    private String department;
    private String status;
    private LocalDate startDate;
}
