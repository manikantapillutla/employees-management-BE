package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.TrainingEnrollmentRequest;
import com.ems.employee_backend.dto.TrainingEnrollmentResponse;
import com.ems.employee_backend.dto.TrainingRequest;
import com.ems.employee_backend.dto.TrainingResponse;
import com.ems.employee_backend.entity.Employee;
import com.ems.employee_backend.entity.Training;
import com.ems.employee_backend.entity.TrainingEnrollment;
import com.ems.employee_backend.repository.EmployeeRepository;
import com.ems.employee_backend.repository.TrainingEnrollmentRepository;
import com.ems.employee_backend.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingEnrollmentRepository enrollmentRepository;
    private final EmployeeRepository employeeRepository;

    public TrainingResponse create(TrainingRequest request) {
        Employee createdBy = employeeRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Training training = Training.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .instructor(request.getInstructor())
                .location(request.getLocation())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .category(request.getCategory())
                .type(request.getType())
                .maxParticipants(request.getMaxParticipants())
                .materials(request.getMaterials())
                .cost(request.getCost())
                .prerequisites(request.getPrerequisites())
                .objectives(request.getObjectives())
                .createdBy(createdBy)
                .status("Draft")
                .build();

        Training savedTraining = trainingRepository.save(training);
        return convertToResponse(savedTraining);
    }

    public List<TrainingResponse> getAll() {
        return trainingRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public TrainingResponse getById(Long id) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training not found"));
        return convertToResponse(training);
    }

    public List<TrainingResponse> getUpcomingTrainings() {
        return trainingRepository.findUpcomingTrainings(LocalDateTime.now()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TrainingResponse> getAvailableTrainings() {
        return trainingRepository.findAvailableTrainings().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TrainingResponse> getTrainingCalendar(LocalDateTime startDate, LocalDateTime endDate) {
        return trainingRepository.findTrainingCalendar(startDate, endDate).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public TrainingEnrollmentResponse enroll(Long trainingId, Long employeeId, Long enrolledBy) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found"));
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        Employee enrolledByEmployee = employeeRepository.findById(enrolledBy)
                .orElseThrow(() -> new RuntimeException("Enroller not found"));

        TrainingEnrollment enrollment = TrainingEnrollment.builder()
                .training(training)
                .employee(employee)
                .enrolledBy(enrolledByEmployee)
                .status("Enrolled")
                .enrolledAt(LocalDateTime.now())
                .build();

        TrainingEnrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return convertEnrollmentToResponse(savedEnrollment);
    }

    public List<TrainingEnrollmentResponse> getEnrollments(Long trainingId, Long employeeId) {
        List<TrainingEnrollment> enrollments;
        if (trainingId != null) {
            enrollments = enrollmentRepository.findByTrainingId(trainingId);
        } else if (employeeId != null) {
            enrollments = enrollmentRepository.findByEmployeeId(employeeId);
        } else {
            enrollments = enrollmentRepository.findAll();
        }
        
        return enrollments.stream()
                .map(this::convertEnrollmentToResponse)
                .collect(Collectors.toList());
    }

    private TrainingResponse convertToResponse(Training training) {
        TrainingResponse response = new TrainingResponse();
        response.setId(training.getId());
        response.setTitle(training.getTitle());
        response.setDescription(training.getDescription());
        response.setInstructor(training.getInstructor());
        response.setLocation(training.getLocation());
        response.setStartDate(training.getStartDate());
        response.setEndDate(training.getEndDate());
        response.setCategory(training.getCategory());
        response.setType(training.getType());
        response.setMaxParticipants(training.getMaxParticipants());
        response.setCurrentParticipants(training.getCurrentParticipants());
        response.setStatus(training.getStatus());
        response.setMaterials(training.getMaterials());
        response.setCost(training.getCost());
        response.setPrerequisites(training.getPrerequisites());
        response.setObjectives(training.getObjectives());
        response.setCreatedByName(training.getCreatedBy() != null ? 
                training.getCreatedBy().getFirstName() + " " + training.getCreatedBy().getLastName() : null);
        response.setCreatedAt(training.getCreatedAt());
        response.setLastModified(training.getLastModified());
        return response;
    }

    private TrainingEnrollmentResponse convertEnrollmentToResponse(TrainingEnrollment enrollment) {
        TrainingEnrollmentResponse response = new TrainingEnrollmentResponse();
        response.setId(enrollment.getId());
        response.setTrainingId(enrollment.getTraining().getId());
        response.setTrainingTitle(enrollment.getTraining().getTitle());
        response.setEmployeeId(enrollment.getEmployee().getId());
        response.setEmployeeName(enrollment.getEmployee().getFirstName() + " " + enrollment.getEmployee().getLastName());
        response.setEnrolledById(enrollment.getEnrolledBy().getId());
        response.setEnrolledByName(enrollment.getEnrolledBy().getFirstName() + " " + enrollment.getEnrolledBy().getLastName());
        response.setStatus(enrollment.getStatus());
        response.setEnrolledAt(enrollment.getEnrolledAt());
        response.setCompletedAt(enrollment.getCompletedAt());
        response.setScore(enrollment.getScore());
        response.setFeedback(enrollment.getFeedback());
        response.setRating(enrollment.getRating());
        response.setCertificateUrl(enrollment.getCertificateUrl());
        response.setCreatedAt(enrollment.getCreatedAt());
        return response;
    }
}
