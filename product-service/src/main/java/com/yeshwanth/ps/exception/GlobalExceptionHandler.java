package com.yeshwanth.ps.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exp) {
        log.error("Validation error occurred", exp);// Create a map to hold error messages
        var errors = new HashMap<String, String>();

        // Iterate over all validation errors
        exp.getBindingResult().getAllErrors().forEach(error -> {
            var fieldName = ((FieldError) error).getField(); // Get the field name
            var errorMessage = error.getDefaultMessage();   // Get the error message
            errors.put(fieldName, errorMessage);             // Add to the map
        });
        // Create an ErrorResponse object
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exp.getStatusCode().toString())
                .message("Invalid input parameters")
                .validationErrors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<ErrorResponse> handle(ResponseStatusException exp) {
//        log.error("ResponseStatusException occurred: {}", exp.getReason(), exp);
//
//        // Create error response for ResponseStatusException
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .timestamp(LocalDateTime.now())
//                .status(exp.getStatusCode().value())
//                .error(exp.getStatusCode().toString())
//                .message(exp.getReason() != null ? exp.getReason() : "An error occurred")
//                .build();
//        return new ResponseEntity<>(errorResponse, exp.getStatusCode());
//    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handle(FileUploadException exp) {
        log.error("File upload exception occurred", exp);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("File Upload Failed")
                .message(exp.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handle(InvalidFileException exp) {
        log.error("Invalid file exception occurred", exp);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid File")
                .message(exp.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
