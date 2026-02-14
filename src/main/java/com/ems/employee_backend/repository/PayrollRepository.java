package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    
    List<Payroll> findByEmployeeId(Long employeeId);
    
    @Query("SELECT p FROM Payroll p WHERE p.payPeriodStart = :payPeriod OR p.payPeriodEnd = :payPeriod")
    List<Payroll> findByPayPeriod(@Param("payPeriod") String payPeriod);
    
    List<Payroll> findByStatus(String status);
    
    @Query("SELECT p FROM Payroll p WHERE p.employee.id = :employeeId AND (p.payPeriodStart = :payPeriod OR p.payPeriodEnd = :payPeriod)")
    Payroll findByEmployeeIdAndPayPeriod(@Param("employeeId") Long employeeId, @Param("payPeriod") String payPeriod);
    
    @Query("SELECT p FROM Payroll p WHERE p.status = :status ORDER BY p.payPeriodStart DESC")
    List<Payroll> findByStatusOrderByPayPeriodDesc(@Param("status") String status);
}
