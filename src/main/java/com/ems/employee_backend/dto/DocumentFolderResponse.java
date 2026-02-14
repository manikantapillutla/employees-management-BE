package com.ems.employee_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DocumentFolderResponse {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private List<DocumentFolderResponse> children;
}
