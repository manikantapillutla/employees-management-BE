package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.NotificationRequest;
import com.ems.employee_backend.dto.NotificationResponse;
import com.ems.employee_backend.dto.SystemAlertRequest;
import com.ems.employee_backend.dto.SystemAlertResponse;
import com.ems.employee_backend.entity.Employee;
import com.ems.employee_backend.entity.Notification;
import com.ems.employee_backend.entity.SystemAlert;
import com.ems.employee_backend.repository.EmployeeRepository;
import com.ems.employee_backend.repository.NotificationRepository;
import com.ems.employee_backend.repository.SystemAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SystemAlertRepository systemAlertRepository;
    private final EmployeeRepository employeeRepository;

    public NotificationResponse create(NotificationRequest request) {
        Employee user = employeeRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .user(user)
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .category(request.getCategory())
                .actionUrl(request.getActionUrl())
                .actionText(request.getActionText())
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return convertToResponse(savedNotification);
    }

    public List<NotificationResponse> getAll(Long userId, Boolean unreadOnly) {
        List<Notification> notifications;
        if (unreadOnly != null && unreadOnly) {
            notifications = notificationRepository.findUnreadByUserId(userId);
        } else {
            notifications = notificationRepository.findByUserId(userId);
        }
        
        return notifications.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponse getById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return convertToResponse(notification);
    }

    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());

        Notification updatedNotification = notificationRepository.save(notification);
        return convertToResponse(updatedNotification);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findUnreadByUserId(userId);
        unreadNotifications.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
        });
        notificationRepository.saveAll(unreadNotifications);
    }

    public Long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    public SystemAlertResponse createAlert(SystemAlertRequest request) {
        Employee createdBy = employeeRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SystemAlert alert = SystemAlert.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .category(request.getCategory())
                .startsAt(request.getStartsAt())
                .endsAt(request.getEndsAt())
                .createdBy(createdBy)
                .build();

        SystemAlert savedAlert = systemAlertRepository.save(alert);
        return convertAlertToResponse(savedAlert);
    }

    public List<SystemAlertResponse> getActiveAlerts() {
        return systemAlertRepository.findActiveAlerts(LocalDateTime.now()).stream()
                .map(this::convertAlertToResponse)
                .collect(Collectors.toList());
    }

    public List<SystemAlertResponse> getAllAlerts() {
        return systemAlertRepository.findByIsActive(true).stream()
                .map(this::convertAlertToResponse)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getNotificationStats(Long userId) {
        Long totalNotifications = notificationRepository.findByUserId(userId).stream().count();
        Long unreadCount = notificationRepository.countUnreadByUserId(userId);
        Long readCount = totalNotifications - unreadCount;

        Map<String, Object> stats = Map.of(
                "totalNotifications", totalNotifications,
                "unreadNotifications", unreadCount,
                "readNotifications", readCount,
                "unreadPercentage", totalNotifications > 0 ? (unreadCount * 100.0 / totalNotifications) : 0
        );

        return stats;
    }

    private NotificationResponse convertToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setUserId(notification.getUser().getId());
        response.setUserName(notification.getUser().getFirstName() + " " + notification.getUser().getLastName());
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setType(notification.getType());
        response.setCategory(notification.getCategory());
        response.setActionUrl(notification.getActionUrl());
        response.setActionText(notification.getActionText());
        response.setIsRead(notification.getIsRead());
        response.setReadAt(notification.getReadAt());
        response.setCreatedAt(notification.getCreatedAt());
        response.setExpiresAt(notification.getExpiresAt());
        return response;
    }

    private SystemAlertResponse convertAlertToResponse(SystemAlert alert) {
        SystemAlertResponse response = new SystemAlertResponse();
        response.setId(alert.getId());
        response.setTitle(alert.getTitle());
        response.setMessage(alert.getMessage());
        response.setType(alert.getType());
        response.setCategory(alert.getCategory());
        response.setIsActive(alert.getIsActive());
        response.setCreatedAt(alert.getCreatedAt());
        response.setStartsAt(alert.getStartsAt());
        response.setEndsAt(alert.getEndsAt());
        response.setCreatedByName(alert.getCreatedBy() != null ? 
                alert.getCreatedBy().getFirstName() + " " + alert.getCreatedBy().getLastName() : null);
        return response;
    }
}
