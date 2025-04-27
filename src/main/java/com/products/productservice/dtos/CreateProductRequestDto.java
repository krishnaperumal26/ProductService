package com.products.productservice.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for creating a new product.
 * Encapsulates the details required to create a product.
 */
@Data
public class CreateProductRequestDto {

    /**
     * The name of the product.
     */
    private String name;

    /**
     * A brief description of the product.
     */
    private String description;

    /**
     * The category to which the product belongs.
     */
    private String category;

    /**
     * The price of the product.
     */
    private double price;

    /**
     * The URL of the product's image.
     */
    private String imageUrl;

}