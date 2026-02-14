package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    List<Project> findByManagerId(Long managerId);
    
    List<Project> findByStatus(String status);
}
