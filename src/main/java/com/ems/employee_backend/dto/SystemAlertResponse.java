package com.ems.employee_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemAlertResponse {
    private Long id;
    private String title;
    private String message;
    private String type;
    private String category;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private String createdByName;
}
