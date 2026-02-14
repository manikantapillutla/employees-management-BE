package com.ems.employee_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String title;
    private String description;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String category;
    private String tags;
    private LocalDateTime uploadedAt;
    private LocalDateTime lastModified;
    private String uploadedBy;
    private Boolean isPublic;
    private String version;
}
