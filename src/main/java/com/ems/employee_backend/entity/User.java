//package com.ems.employee_backend.entity;
//
//import jakarta.persistence.*;
//import org.springframework.cglib.core.Local;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "user")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String username;
//    private String password;
//    private String role;
//    private LocalDateTime createdDate;
//    private LocalDateTime lastUpdatedTime;
//
//    @PrePersist
//    protected void onCreate(){
//        this.createdDate=LocalDateTime.now();
//        this.lastUpdatedTime=LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate(){
//        this.lastUpdatedTime=LocalDateTime.now();
//    }
//
//    public User() {
//    }
//
//    public LocalDateTime getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(LocalDateTime createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public LocalDateTime getLastUpdatedTime() {
//        return lastUpdatedTime;
//    }
//
//    public void setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
//        this.lastUpdatedTime = lastUpdatedTime;
//    }
//
//    public User(Long id, String username, String password, String role, LocalDateTime createdDate, LocalDateTime lastUpdatedTime) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.role = role;
//        this.createdDate = createdDate;
//        this.lastUpdatedTime = lastUpdatedTime;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//// getters & setters
//}
