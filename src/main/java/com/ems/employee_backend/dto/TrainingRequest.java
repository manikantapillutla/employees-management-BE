package com.ems.employee_backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TrainingRequest {
    private String title;
    private String description;
    private String instructor;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String category;
    private String type;
    private Integer maxParticipants;
    private String materials;
    private Double cost;
    private String prerequisites;
    private String objectives;
    private Long createdById;
}
