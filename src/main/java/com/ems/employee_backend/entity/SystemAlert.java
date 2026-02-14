package com.ems.employee_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_alerts")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemAlert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String message;
    private String type; // Info, Warning, Critical
    private String category; // System, Maintenance, Security
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private Employee createdBy;
}
