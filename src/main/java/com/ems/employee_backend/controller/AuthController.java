package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.AuthRequest;
import com.ems.employee_backend.dto.ChangePasswordRequest;
import com.ems.employee_backend.dto.UserResponse;
import com.ems.employee_backend.service.CustomUserDetailsService;
import com.ems.employee_backend.service.UserService;
import com.ems.employee_backend.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> generateToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(authRequest.getUsername());
            
            UserResponse response = new UserResponse();
//            response.setId(userDetails);
            response.setUsername(userDetails.getUsername());
            response.setRole(userDetails.getAuthorities().iterator().next().getAuthority());
            response.setToken(token);
            response.setMessage("Login successful");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // For JWT, logout is typically handled client-side by removing token
        // We can add token blacklisting if needed in production
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserResponse> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            
            try {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(username, userDetails, token)) {
                    String newToken = jwtUtil.generateToken(username);
                    
                    UserResponse response = new UserResponse();
                    response.setUsername(userDetails.getUsername());
                    response.setRole(userDetails.getAuthorities().iterator().next().getAuthority());
                    response.setToken(newToken);
                    response.setMessage("Token refreshed successfully");
                    
                    return ResponseEntity.ok(response);
                }
            } catch (Exception e) {
                throw new RuntimeException("Invalid token for refresh");
            }
        }
        throw new RuntimeException("No valid token provided");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        userService.changePassword(currentUsername, request.getCurrentPassword(), request.getNewPassword());
        
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        UserResponse response = new UserResponse();
        response.setUsername(userDetails.getUsername());
        response.setRole(userDetails.getAuthorities().iterator().next().getAuthority());
        response.setMessage("Profile retrieved successfully");
        
        return ResponseEntity.ok(response);
    }
}
