package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    boolean existsByName(String name);
}
