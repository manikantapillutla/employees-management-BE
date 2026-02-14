package com.ems.employee_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Employee user;
    
    private String title;
    private String message;
    private String type; // task, training, performance, leave, payroll, meeting, system, security, finance, policy, holiday, project, document
    private String priority; // low, medium, high, urgent
    @Column(name = "is_read")
    private Boolean isRead = false;
    @Column(name = "related_entity_type")
    private String relatedEntityType;
    @Column(name = "related_entity_id")
    private Long relatedEntityId;
    @Column(name = "action_url")
    private String actionUrl;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    private LocalDateTime readAt;
    private String category;
    private String actionText;
    private LocalDateTime expiresAt;
    
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
