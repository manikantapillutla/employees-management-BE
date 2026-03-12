package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.RegisterUserRequest;
import com.ems.employee_backend.dto.UserResponse;
import com.ems.employee_backend.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    
    private final UserRegisterService userRegisterService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        registerUserRequest.setRole("USER");
        UserResponse userResponse = userRegisterService.registerUser(registerUserRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<UserResponse> registerByAdmin(@RequestBody RegisterUserRequest registerUserRequest) {
        UserResponse userResponse = userRegisterService.registerUser(registerUserRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }


}
