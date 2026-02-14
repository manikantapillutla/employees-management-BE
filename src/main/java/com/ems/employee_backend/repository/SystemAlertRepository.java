package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.SystemAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemAlertRepository extends JpaRepository<SystemAlert, Long> {
    
    List<SystemAlert> findByIsActive(Boolean isActive);
    
    @Query("SELECT sa FROM SystemAlert sa WHERE sa.isActive = true AND " +
           "(sa.startsAt IS NULL OR sa.startsAt <= :now) AND " +
           "(sa.endsAt IS NULL OR sa.endsAt >= :now)")
    List<SystemAlert> findActiveAlerts(@Param("now") LocalDateTime now);
    
    @Query("SELECT sa FROM SystemAlert sa WHERE sa.category = :category AND sa.isActive = true")
    List<SystemAlert> findByCategory(@Param("category") String category);
}
