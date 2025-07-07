package com.careertone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getErrorMessage());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    private ResponseEntity<ErrorResponse> getErrorResponse(HttpStatus status, String message) {
        ErrorResponse response = new ErrorResponse(
                status.name(),
                message
        );
        return new ResponseEntity<>(response, status);
    }
}
