package com.ems.employee_backend.controller;

import com.ems.employee_backend.dto.DocumentRequest;
import com.ems.employee_backend.dto.DocumentResponse;
import com.ems.employee_backend.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Document Management", description = "APIs for managing documents")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    @Operation(summary = "Create document", description = "Create a new document record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Document created successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody DocumentRequest request) {
        return new ResponseEntity<>(documentService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all documents", description = "Retrieve all documents")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documents retrieved successfully")
    })
    public ResponseEntity<List<DocumentResponse>> getAllDocuments() {
        return new ResponseEntity<>(documentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document by ID", description = "Retrieve a specific document")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public ResponseEntity<DocumentResponse> getDocumentById(
            @Parameter(description = "Document ID") @PathVariable Long id) {
        return new ResponseEntity<>(documentService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get documents by employee", description = "Retrieve documents for a specific employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documents retrieved successfully")
    })
    public ResponseEntity<List<DocumentResponse>> getDocumentsByEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        return new ResponseEntity<>(documentService.getByEmployee(employeeId), HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search documents", description = "Search documents by title, description, or tags")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<List<DocumentResponse>> searchDocuments(
            @Parameter(description = "Search query") @RequestParam String query) {
        return new ResponseEntity<>(documentService.search(query), HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get documents by category", description = "Retrieve documents by category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documents retrieved successfully")
    })
    public ResponseEntity<List<DocumentResponse>> getDocumentsByCategory(
            @Parameter(description = "Category") @PathVariable String category) {
        return new ResponseEntity<>(documentService.getByCategory(category), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update document", description = "Update an existing document")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document updated successfully"),
        @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public ResponseEntity<DocumentResponse> updateDocument(
            @Parameter(description = "Document ID") @PathVariable Long id,
            @RequestBody DocumentRequest request) {
        return new ResponseEntity<>(documentService.update(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete document", description = "Delete a document from system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public ResponseEntity<Void> deleteDocument(
            @Parameter(description = "Document ID") @PathVariable Long id) {
        documentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
