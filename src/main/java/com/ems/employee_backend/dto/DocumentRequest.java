package com.ems.employee_backend.dto;

import lombok.Data;

@Data
public class DocumentRequest {
    private Long employeeId;
    private String title;
    private String description;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String category;
    private String tags;
    private Boolean isPublic;
    private String uploadedBy;
}
