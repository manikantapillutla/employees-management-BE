package com.ems.employee_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentFolderRequest {
    private String name;
    private String description;
    private Long parentId;
    private Long createdById;
}
