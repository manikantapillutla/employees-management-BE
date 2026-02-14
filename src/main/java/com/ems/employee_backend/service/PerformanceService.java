package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.PerformanceRequest;
import com.ems.employee_backend.dto.PerformanceResponse;
import com.ems.employee_backend.entity.Employee;
import com.ems.employee_backend.entity.Performance;
import com.ems.employee_backend.repository.EmployeeRepository;
import com.ems.employee_backend.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final EmployeeRepository employeeRepository;

    public PerformanceResponse create(PerformanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        Employee reviewer = employeeRepository.findById(request.getReviewerId())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        Performance performance = Performance.builder()
                .employee(employee)
                .reviewer(reviewer)
                .reviewPeriodStart(request.getReviewPeriodStart())
                .reviewPeriodEnd(request.getReviewPeriodEnd())
                .overallRating(request.getOverallRating())
                .technicalSkills(request.getTechnicalSkills())
                .communication(request.getCommunication())
                .teamwork(request.getTeamwork())
                .leadership(request.getLeadership())
                .problemSolving(request.getProblemSolving())
                .timeManagement(request.getTimeManagement())
                .comments(request.getComments())
                .status("pending")
                .build();

        Performance savedPerformance = performanceRepository.save(performance);
        return convertToResponse(savedPerformance);
    }

    public List<PerformanceResponse> getAll() {
        return performanceRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PerformanceResponse getById(Long id) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance review not found"));
        return convertToResponse(performance);
    }

    public List<PerformanceResponse> getByEmployeeId(Long employeeId) {
        return performanceRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PerformanceResponse> getByReviewPeriod(String reviewPeriod) {
        return performanceRepository.findAll().stream()
                .filter(p -> reviewPeriod.equals(p.getReviewPeriodStart().toString() + " - " + p.getReviewPeriodEnd().toString()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PerformanceResponse update(Long id, PerformanceRequest request) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance review not found"));

        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            performance.setEmployee(employee);
        }

        if (request.getReviewerId() != null) {
            Employee reviewer = employeeRepository.findById(request.getReviewerId())
                    .orElseThrow(() -> new RuntimeException("Reviewer not found"));
            performance.setReviewer(reviewer);
        }

        if (request.getReviewPeriodStart() != null) performance.setReviewPeriodStart(request.getReviewPeriodStart());
        if (request.getReviewPeriodEnd() != null) performance.setReviewPeriodEnd(request.getReviewPeriodEnd());
        if (request.getOverallRating() != null) performance.setOverallRating(request.getOverallRating());
        if (request.getTechnicalSkills() != null) performance.setTechnicalSkills(request.getTechnicalSkills());
        if (request.getCommunication() != null) performance.setCommunication(request.getCommunication());
        if (request.getTeamwork() != null) performance.setTeamwork(request.getTeamwork());
        if (request.getLeadership() != null) performance.setLeadership(request.getLeadership());
        if (request.getProblemSolving() != null) performance.setProblemSolving(request.getProblemSolving());
        if (request.getTimeManagement() != null) performance.setTimeManagement(request.getTimeManagement());
        if (request.getComments() != null) performance.setComments(request.getComments());
        if (request.getStatus() != null) performance.setStatus(request.getStatus());

        Performance updatedPerformance = performanceRepository.save(performance);
        return convertToResponse(updatedPerformance);
    }

    public void delete(Long id) {
        if (!performanceRepository.existsById(id)) {
            throw new RuntimeException("Performance review not found");
        }
        performanceRepository.deleteById(id);
    }

    private PerformanceResponse convertToResponse(Performance performance) {
        PerformanceResponse response = new PerformanceResponse();
        response.setId(performance.getId());
        response.setEmployeeId(performance.getEmployee().getId());
        response.setEmployeeName(performance.getEmployee().getFirstName() + " " + performance.getEmployee().getLastName());
        response.setReviewerId(performance.getReviewer().getId());
        response.setReviewerName(performance.getReviewer().getFirstName() + " " + performance.getReviewer().getLastName());
        response.setReviewPeriodStart(performance.getReviewPeriodStart());
        response.setReviewPeriodEnd(performance.getReviewPeriodEnd());
        response.setOverallRating(performance.getOverallRating());
        response.setTechnicalSkills(performance.getTechnicalSkills());
        response.setCommunication(performance.getCommunication());
        response.setTeamwork(performance.getTeamwork());
        response.setLeadership(performance.getLeadership());
        response.setProblemSolving(performance.getProblemSolving());
        response.setTimeManagement(performance.getTimeManagement());
        response.setComments(performance.getComments());
        response.setStatus(performance.getStatus());
        response.setCreatedAt(performance.getCreatedAt());
        response.setUpdatedAt(performance.getUpdatedAt());
        return response;
    }
}
