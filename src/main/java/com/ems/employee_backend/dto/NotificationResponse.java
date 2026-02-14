package com.ems.employee_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String message;
    private String type;
    private String category;
    private String actionUrl;
    private String actionText;
    private Boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
