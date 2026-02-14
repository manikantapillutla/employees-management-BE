package com.ems.employee_backend.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private Long userId;
    private String title;
    private String message;
    private String type;
    private String category;
    private String actionUrl;
    private String actionText;
}
