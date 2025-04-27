package com.products.productservice.exception;

import com.products.productservice.dtos.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 * Provides centralized exception handling for all controllers.
 * Uses @RestControllerAdvice to apply to all REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles NullPointerException and returns a standardized error response.
     *
     * @return A ResponseEntity containing an ErrorDto with error details and HTTP status 500.
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException() {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("Null pointer exception");
        errorDto.setStatus("Failure..");

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles ProductNotFoundException and returns a standardized error response.
     *
     * @param e The ProductNotFoundException instance containing error details.
     * @return A ResponseEntity containing an ErrorDto with error details and HTTP status 404.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(ProductNotFoundException e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(e.getMessage());
        errorDto.setStatus("Failure..");
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }
}