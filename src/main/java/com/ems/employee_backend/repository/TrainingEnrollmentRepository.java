package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.TrainingEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingEnrollmentRepository extends JpaRepository<TrainingEnrollment, Long> {
    
    List<TrainingEnrollment> findByTrainingId(Long trainingId);
    
    List<TrainingEnrollment> findByEmployeeId(Long employeeId);
    
    List<TrainingEnrollment> findByTrainingIdAndEmployeeId(Long trainingId, Long employeeId);
    
    @Query("SELECT te FROM TrainingEnrollment te WHERE te.training.id = :trainingId AND te.employee.id = :employeeId")
    TrainingEnrollment findByTrainingAndEmployee(@Param("trainingId") Long trainingId, @Param("employeeId") Long employeeId);
    
    @Query("SELECT te FROM TrainingEnrollment te WHERE te.status = :status")
    List<TrainingEnrollment> findByStatus(@Param("status") String status);
}
