package com.ems.employee_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String role;
//    private String email;
//    private String firstName;
//    private String lastName;
//    private String phone;
    private String token;
    private String message;
//    private String createdAt;
//    private String updatedAt;

    // Constructor for backward compatibility
    public UserResponse(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
