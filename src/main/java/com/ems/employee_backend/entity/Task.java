package com.ems.employee_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    
    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private Employee employee;
    
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Employee createdBy;
    
    private String priority; // low, medium, high, urgent
    private String status; // not_started, in_progress, completed, cancelled, on_hold
    private LocalDate dueDate;
    @Column(name = "estimated_hours")
    private Double estimatedHours;
    @Column(name = "actual_hours")
    private Double actualHours;
    private Integer progress;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private LocalDate completedDate;

    private String notes;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "not_started";
        }
        if (progress == null) {
            progress = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
