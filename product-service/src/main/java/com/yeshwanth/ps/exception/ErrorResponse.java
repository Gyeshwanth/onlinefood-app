package com.yeshwanth.ps.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Only include non-null fields in JSON
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;

    // For validation errors
    private Map<String, String> validationErrors;

    // For detailed error information
    private String details;

    // Constructor for simple errors (backward compatibility)
    public ErrorResponse(Map<String, String> errors) {
        this.validationErrors = errors;
        this.timestamp = LocalDateTime.now();
    }
}
