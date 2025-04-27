package com.products.productservice.exception;

/**
 * Custom exception class to handle cases where a product is not found.
 * Extends the Exception class to provide additional context for the error.
 */
public class ProductNotFoundException extends Exception {

    /**
     * Constructs a new ProductNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}