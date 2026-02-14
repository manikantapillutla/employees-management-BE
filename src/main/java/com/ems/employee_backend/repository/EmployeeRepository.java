package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Long countByDepartmentId(Long departmentId);
    
    List<Employee> findByDepartmentId(Long departmentId); 
    
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.jobTitle) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Employee> searchEmployees(@Param("query") String query);
    
    @Query("SELECT e FROM Employee e WHERE e.departmentId = " +
           "(SELECT d.id FROM Department d WHERE d.name = :department)")
    List<Employee> findByDepartmentName(@Param("department") String department);
    
    List<Employee> findByStatus(String status);
}
