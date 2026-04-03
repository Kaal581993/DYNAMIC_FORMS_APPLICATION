package com.form_builder.User_Service.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {

        ApiErrorResponse error = ApiErrorResponse.builder()
                .message(ex.getMessage())
                .status(404)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {

        ApiErrorResponse error = ApiErrorResponse.builder()
                .message(ex.getMessage())
                .status(500)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(500).body(error);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicate(DuplicateResourceException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .message(ex.getMessage())
                .status(409)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .message("Resource already exists")
                .status(409)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(409).body(error);
    }

}
