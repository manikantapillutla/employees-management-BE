package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.EmployeeRequest;
import com.ems.employee_backend.dto.EmployeeResponse;
import com.ems.employee_backend.entity.Department;
import com.ems.employee_backend.entity.Employee;
import com.ems.employee_backend.repository.DepartmentRepository;
import com.ems.employee_backend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeResponse create(EmployeeRequest request) {
        Employee emp = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .jobTitle(request.getPosition())
                .status(request.getStatus())
                .hireDate(request.getStartDate())
                .departmentId(request.getDepartmentId())
                .build();

        Employee savedEmployee = employeeRepository.save(emp);
        return convertToResponse(savedEmployee);
    }
    public List<EmployeeResponse> getAll() {
        return employeeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return convertToResponse(employee);
    }

    public List<EmployeeResponse> searchEmployees(String query) {
        return employeeRepository.searchEmployees(query).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<EmployeeResponse> filterByDepartment(String department) {
        return employeeRepository.findByDepartmentName(department).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<EmployeeResponse> filterByStatus(String status) {
        return employeeRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if (request.getFirstName() != null) employee.setFirstName(request.getFirstName());
        if (request.getLastName() != null) employee.setLastName(request.getLastName());
        if (request.getEmail() != null) employee.setEmail(request.getEmail());
        if (request.getPosition() != null) employee.setJobTitle(request.getPosition());
        if (request.getStatus() != null) employee.setStatus(request.getStatus());
        if (request.getStartDate() != null) employee.setHireDate(request.getStartDate());
        if (request.getDepartmentId() != null) employee.setDepartmentId(request.getDepartmentId());
        
        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToResponse(updatedEmployee);
    }

    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }

    private EmployeeResponse convertToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPosition(employee.getJobTitle());
        response.setDepartmentId(employee.getDepartmentId());
        
        // Set department name if departmentId exists
        if (employee.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employee.getDepartmentId())
                    .orElse(null);
            response.setDepartment(department != null ? department.getName() : null);
        }
        
        response.setStatus(employee.getStatus());
        response.setStartDate(employee.getHireDate());
        return response;
    }



}
