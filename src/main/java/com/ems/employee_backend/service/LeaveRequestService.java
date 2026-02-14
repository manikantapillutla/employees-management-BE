package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.LeaveRequestRequest;
import com.ems.employee_backend.dto.LeaveRequestResponse;
import com.ems.employee_backend.entity.Employee;
import com.ems.employee_backend.entity.LeaveRequest;
import com.ems.employee_backend.repository.EmployeeRepository;
import com.ems.employee_backend.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestResponse create(LeaveRequestRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .employee(employee)
                .leaveType(request.getLeaveType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status("pending")
                .build();

        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);
        return convertToResponse(savedLeaveRequest);
    }

    public List<LeaveRequestResponse> getAll() {
        return leaveRequestRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public LeaveRequestResponse getById(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        return convertToResponse(leaveRequest);
    }

    public List<LeaveRequestResponse> getByEmployeeId(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestResponse> getByStatus(String status) {
        return leaveRequestRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public LeaveRequestResponse approve(Long id, Long approvedById) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        Employee approvedBy = employeeRepository.findById(approvedById)
                .orElseThrow(() -> new RuntimeException("Approving employee not found"));

        leaveRequest.setStatus("approved");
        leaveRequest.setApprovedBy(approvedBy);

        LeaveRequest updatedLeaveRequest = leaveRequestRepository.save(leaveRequest);
        return convertToResponse(updatedLeaveRequest);
    }

    public LeaveRequestResponse reject(Long id, String comments) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        leaveRequest.setStatus("rejected");

        LeaveRequest updatedLeaveRequest = leaveRequestRepository.save(leaveRequest);
        return convertToResponse(updatedLeaveRequest);
    }

    public void delete(Long id) {
        if (!leaveRequestRepository.existsById(id)) {
            throw new RuntimeException("Leave request not found");
        }
        leaveRequestRepository.deleteById(id);
    }

    private LeaveRequestResponse convertToResponse(LeaveRequest leaveRequest) {
        LeaveRequestResponse response = new LeaveRequestResponse();
        response.setId(leaveRequest.getId());
        response.setEmployeeId(leaveRequest.getEmployee().getId());
        response.setEmployeeName(leaveRequest.getEmployee().getFirstName() + " " + leaveRequest.getEmployee().getLastName());
        response.setLeaveType(leaveRequest.getLeaveType());
        response.setStartDate(leaveRequest.getStartDate());
        response.setEndDate(leaveRequest.getEndDate());
        response.setReason(leaveRequest.getReason());
        response.setStatus(leaveRequest.getStatus());
        response.setApprovedById(leaveRequest.getApprovedBy() != null ? leaveRequest.getApprovedBy().getId() : null);
        response.setApprovedByName(leaveRequest.getApprovedBy() != null ? 
                leaveRequest.getApprovedBy().getFirstName() + " " + leaveRequest.getApprovedBy().getLastName() : null);
        response.setApprovedAt(leaveRequest.getApprovedAt());
        response.setCreatedAt(leaveRequest.getCreatedAt());
        response.setUpdatedAt(leaveRequest.getUpdatedAt());
        return response;
    }
}
