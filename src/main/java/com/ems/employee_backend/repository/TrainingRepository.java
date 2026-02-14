package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    
    List<Training> findByStatus(String status);
    
    @Query("SELECT t FROM Training t WHERE t.startDate >= :startDate ORDER BY t.startDate ASC")
    List<Training> findUpcomingTrainings(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT t FROM Training t WHERE t.startDate BETWEEN :startDate AND :endDate ORDER BY t.startDate ASC")
    List<Training> findTrainingCalendar(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM Training t WHERE " +
           "LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.category) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Training> searchTrainings(@Param("query") String query);
    
    @Query("SELECT t FROM Training t WHERE t.maxParticipants > t.currentParticipants")
    List<Training> findAvailableTrainings();
}
