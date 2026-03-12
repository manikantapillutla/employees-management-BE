package com.ems.employee_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", 
             message = "Password must be at least 8 characters with uppercase, lowercase, and number")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;

    @Email(message = "Invalid email format")
    private String email;

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^\\+?[\\d\\s\\-\\(\\)]+$", message = "Invalid phone number format")
    private String phone;
}
