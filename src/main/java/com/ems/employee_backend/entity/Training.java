package com.ems.employee_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "training_programs")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private String type; // online, workshop, seminar, certification, mentorship
    private String category; // technical, soft-skills, leadership, compliance, safety
    private Integer duration; // in hours
    private Double cost;
    private String provider;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    @Column(name = "max_participants")
    private Integer maxParticipants;
    @Column(name = "current_participants")
    private Integer currentParticipants = 0;
    private String status; // planned, ongoing, completed, cancelled
    private String materials;
    private String instructor;
    private String prerequisites;
    private String objectives;
    private LocalDateTime lastModified;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private Employee createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "planned";
        }
    }
    
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
