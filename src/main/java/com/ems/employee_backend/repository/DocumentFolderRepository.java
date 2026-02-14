package com.ems.employee_backend.repository;

import com.ems.employee_backend.entity.DocumentFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentFolderRepository extends JpaRepository<DocumentFolder, Long> {
    
    List<DocumentFolder> findByParentId(Long parentId);
    
    List<DocumentFolder> findByParentIdIsNull();
    
    List<DocumentFolder> findByCreatedById(Long createdById);
}
