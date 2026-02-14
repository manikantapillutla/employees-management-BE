package com.ems.employee_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "training_enrollments")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingEnrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @ManyToOne
    @JoinColumn(name = "enrolled_by")
    private Employee enrolledBy;
    
    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;
    
    private String status; // enrolled, in_progress, completed, dropped, failed
    @Column(name = "completion_date")
    private LocalDate completedAt;
    private Double score;
    private String feedback;
    private Integer rating; // 1-5 stars
    private String certificate;
    private String certificateUrl;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (enrolledAt == null) {
            enrolledAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "enrolled";
        }
    }
    
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
