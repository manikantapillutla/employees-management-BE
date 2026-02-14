package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    
    List<Performance> findByEmployeeId(Long employeeId);
    
    @Query("SELECT p FROM Performance p WHERE p.reviewPeriodStart = :reviewPeriod OR p.reviewPeriodEnd = :reviewPeriod")
    List<Performance> findByReviewPeriod(@Param("reviewPeriod") String reviewPeriod);
    
    @Query("SELECT p FROM Performance p WHERE p.employee.id = :employeeId ORDER BY p.createdAt DESC")
    List<Performance> findByEmployeeIdOrderByReviewDateDesc(@Param("employeeId") Long employeeId);
    
    @Query("SELECT p FROM Performance p WHERE p.overallRating >= :minRating ORDER BY p.overallRating DESC")
    List<Performance> findByRatingGreaterThanEqual(@Param("minRating") Double minRating);
}
