package com.products.productservice.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing error responses.
 * Encapsulates the status and message of an error to be sent in API responses.
 */
@Data
public class ErrorDto {

    /**
     * The status of the error (e.g., "Failure", "Error").
     */
    private String status;

    /**
     * The detailed message describing the error.
     */
    private String message;
}