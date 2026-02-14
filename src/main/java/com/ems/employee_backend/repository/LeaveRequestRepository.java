package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    
    List<LeaveRequest> findByEmployeeId(Long employeeId);
    
    List<LeaveRequest> findByStatus(String status);
    
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.status = :status AND lr.createdAt BETWEEN :startDate AND :endDate")
    List<LeaveRequest> findByStatusAndAppliedDateBetween(@Param("status") String status, 
                                                         @Param("startDate") LocalDate startDate, 
                                                         @Param("endDate") LocalDate endDate);
    
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = :employeeId AND (:startDate IS NULL OR lr.startDate >= :startDate) AND (:endDate IS NULL OR lr.endDate <= :endDate)")
    List<LeaveRequest> findLeaveRequestsByEmployeeAndDateRange(@Param("employeeId") Long employeeId,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);
}
