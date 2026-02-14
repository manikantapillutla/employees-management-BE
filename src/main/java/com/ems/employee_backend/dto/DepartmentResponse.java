package com.ems.employee_backend.dto;

import lombok.Data;

@Data
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
    private Long employees;
}
