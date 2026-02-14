package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.NotificationRequest;
import com.ems.employee_backend.dto.NotificationResponse;
import com.ems.employee_backend.dto.SystemAlertRequest;
import com.ems.employee_backend.dto.SystemAlertResponse;
import com.ems.employee_backend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Notification System", description = "APIs for managing notifications and alerts")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create notification", description = "Create a new notification")
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody NotificationRequest request) {
        return new ResponseEntity<>(notificationService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get notifications", description = "Retrieve notifications for a user")
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @RequestParam Long userId,
            @RequestParam(required = false) Boolean unreadOnly) {
        return new ResponseEntity<>(notificationService.getAll(userId, unreadOnly), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID", description = "Retrieve a specific notification")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        return new ResponseEntity<>(notificationService.getById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark notification as read", description = "Mark a notification as read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        return new ResponseEntity<>(notificationService.markAsRead(id), HttpStatus.OK);
    }

    @PatchMapping("/user/{userId}/read-all")
    @Operation(summary = "Mark all notifications as read", description = "Mark all notifications for a user as read")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/unread-count")
    @Operation(summary = "Get unread count", description = "Get count of unread notifications for a user")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        return new ResponseEntity<>(notificationService.getUnreadCount(userId), HttpStatus.OK);
    }

    @GetMapping("/stats/{userId}")
    @Operation(summary = "Get notification statistics", description = "Get notification statistics for a user")
    public ResponseEntity<Map<String, Object>> getNotificationStats(@PathVariable Long userId) {
        return new ResponseEntity<>(notificationService.getNotificationStats(userId), HttpStatus.OK);
    }

    @PostMapping("/alerts")
    @Operation(summary = "Create system alert", description = "Create a new system alert")
    public ResponseEntity<SystemAlertResponse> createAlert(@RequestBody SystemAlertRequest request) {
        return new ResponseEntity<>(notificationService.createAlert(request), HttpStatus.CREATED);
    }

    @GetMapping("/alerts")
    @Operation(summary = "Get system alerts", description = "Retrieve all system alerts")
    public ResponseEntity<List<SystemAlertResponse>> getSystemAlerts() {
        return new ResponseEntity<>(notificationService.getAllAlerts(), HttpStatus.OK);
    }

    @GetMapping("/alerts/active")
    @Operation(summary = "Get active alerts", description = "Retrieve currently active system alerts")
    public ResponseEntity<List<SystemAlertResponse>> getActiveAlerts() {
        return new ResponseEntity<>(notificationService.getActiveAlerts(), HttpStatus.OK);
    }
}
