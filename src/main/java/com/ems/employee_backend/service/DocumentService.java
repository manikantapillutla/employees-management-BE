package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.DocumentRequest;
import com.ems.employee_backend.dto.DocumentResponse;
import com.ems.employee_backend.entity.Document;
import com.ems.employee_backend.entity.Employee;
import com.ems.employee_backend.repository.DocumentRepository;
import com.ems.employee_backend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;

    public DocumentResponse create(DocumentRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Document document = Document.builder()
                .employee(employee)
                .title(request.getTitle())
                .description(request.getDescription())
                .fileName(request.getFileName())
                .filePath(request.getFilePath())
                .fileType(request.getFileType())
                .fileSize(request.getFileSize())
                .category(request.getCategory())
                .tags(request.getTags())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .uploadedBy(request.getUploadedBy())
                .build();

        Document savedDocument = documentRepository.save(document);
        return convertToResponse(savedDocument);
    }

    public List<DocumentResponse> getAll() {
        return documentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public DocumentResponse getById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return convertToResponse(document);
    }

    public List<DocumentResponse> getByEmployee(Long employeeId) {
        return documentRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DocumentResponse> search(String query) {
        return documentRepository.searchDocuments(query).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DocumentResponse> getByCategory(String category) {
        return documentRepository.findByCategory(category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public DocumentResponse update(Long id, DocumentRequest request) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (request.getTitle() != null) document.setTitle(request.getTitle());
        if (request.getDescription() != null) document.setDescription(request.getDescription());
        if (request.getCategory() != null) document.setCategory(request.getCategory());
        if (request.getTags() != null) document.setTags(request.getTags());
        if (request.getIsPublic() != null) document.setIsPublic(request.getIsPublic());

        Document updatedDocument = documentRepository.save(document);
        return convertToResponse(updatedDocument);
    }

    public void delete(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new RuntimeException("Document not found");
        }
        documentRepository.deleteById(id);
    }

    private DocumentResponse convertToResponse(Document document) {
        DocumentResponse response = new DocumentResponse();
        response.setId(document.getId());
        response.setEmployeeId(document.getEmployee().getId());
        response.setEmployeeName(document.getEmployee().getFirstName() + " " + document.getEmployee().getLastName());
        response.setTitle(document.getTitle());
        response.setDescription(document.getDescription());
        response.setFileName(document.getFileName());
        response.setFilePath(document.getFilePath());
        response.setFileType(document.getFileType());
        response.setFileSize(document.getFileSize());
        response.setCategory(document.getCategory());
        response.setTags(document.getTags());
        response.setUploadedAt(document.getUploadedAt());
        response.setLastModified(document.getLastModified());
        response.setUploadedBy(document.getUploadedBy());
        response.setIsPublic(document.getIsPublic());
        response.setVersion(document.getVersion());
        return response;
    }
}
