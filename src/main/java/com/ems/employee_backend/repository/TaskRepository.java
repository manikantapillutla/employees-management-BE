package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByEmployeeId(Long employeeId);
    
    List<Task> findByStatus(String status);
    
    List<Task> findByPriority(String priority);
    
    List<Task> findByDueDateBefore(LocalDate date);
    
    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId AND t.status = :status")
    List<Task> findByEmployeeIdAndStatus(@Param("employeeId") Long employeeId, @Param("status") String status);
    
    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId AND t.dueDate <= :date AND t.status != 'completed'")
    List<Task> findOverdueTasksByEmployee(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);
    
    @Query("SELECT t FROM Task t WHERE t.priority = :priority AND t.status != 'completed' ORDER BY t.dueDate ASC")
    List<Task> findIncompleteTasksByPriority(@Param("priority") String priority);
}
