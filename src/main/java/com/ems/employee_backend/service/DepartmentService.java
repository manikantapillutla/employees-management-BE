package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.DepartmentRequest;
import com.ems.employee_backend.dto.DepartmentResponse;
import com.ems.employee_backend.entity.Department;
import com.ems.employee_backend.repository.DepartmentRepository;
import com.ems.employee_backend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department with name '" + request.getName() + "' already exists");
        }
        
        Department dept= Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        Department savedDepartment = departmentRepository.save(dept);
        return convertToResponse(savedDepartment);
    }

    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", departmentRepository.count());
        
        // Count active employees across all departments
        Long activeEmployees = (long)employeeRepository.findByStatus("active").size();
        stats.put("activeEmployees", activeEmployees);
        
        return stats;
    }

    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        
        if (request.getName() != null && !request.getName().equals(department.getName())) {
            if (departmentRepository.existsByName(request.getName())) {
                throw new RuntimeException("Department with name '" + request.getName() + "' already exists");
            }
            department.setName(request.getName());
        }
        
        if (request.getDescription() != null) {
            department.setDescription(request.getDescription());
        }
        
        Department updatedDepartment = departmentRepository.save(department);
        return convertToResponse(updatedDepartment);
    }

    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found");
        }
        
        Long employeeCount = employeeRepository.countByDepartmentId(id);
        if (employeeCount > 0) {
            throw new RuntimeException("Cannot delete department with " + employeeCount + " employees");
        }
        
        departmentRepository.deleteById(id);
    }

    private DepartmentResponse convertToResponse(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setName(department.getName());
        response.setDescription(department.getDescription());
        response.setEmployees(employeeRepository.countByDepartmentId(department.getId()));
        return response;
    }
}
