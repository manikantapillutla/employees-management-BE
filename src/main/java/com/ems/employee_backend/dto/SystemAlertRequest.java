package com.ems.employee_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemAlertRequest {
    private String title;
    private String message;
    private String type;
    private String category;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private Long createdById;
}
