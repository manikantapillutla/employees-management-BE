package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.RegisterUserRequest;
import com.ems.employee_backend.dto.UserResponse;
import com.ems.employee_backend.entity.User;
import com.ems.employee_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserRegisterService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(RegisterUserRequest registerUserRequest) {
        // Check if username already exists
        if (userRepository.existsByUsername(registerUserRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists (if provided)
        if (registerUserRequest.getEmail() != null && 
            !registerUserRequest.getEmail().trim().isEmpty() &&
            userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setRole(registerUserRequest.getRole());
        user.setEmail(registerUserRequest.getEmail());
        user.setFirstName(registerUserRequest.getFirstName());
        user.setLastName(registerUserRequest.getLastName());
        user.setPhone(registerUserRequest.getPhone());

        User savedUser = userRepository.save(user);
        
        return convertToResponse(savedUser, "User registered successfully");
    }

    private UserResponse convertToResponse(User user, String message) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setMessage(message);
        
        if (user.getCreatedAt() != null) {
            response.setCreatedAt(user.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (user.getUpdatedAt() != null) {
            response.setUpdatedAt(user.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        
        return response;
    }


}
