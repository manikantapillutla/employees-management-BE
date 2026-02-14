package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByEmployeeId(Long employeeId);
    
    @Query("SELECT d FROM Document d WHERE " +
           "LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.tags) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Document> searchDocuments(@Param("query") String query);
    
    List<Document> findByCategory(String category);
    
    List<Document> findByIsPublic(Boolean isPublic);
    
    @Query("SELECT d FROM Document d WHERE d.employee.id = :employeeId AND d.category = :category")
    List<Document> findByEmployeeAndCategory(@Param("employeeId") Long employeeId, @Param("category") String category);
}
