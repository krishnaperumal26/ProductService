package com.products.productservice.dtos;

import com.products.productservice.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for representing product responses.
 * Encapsulates product details to be sent in API responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    /**
     * The unique identifier of the product.
     */
    private long id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * A brief description of the product.
     */
    private String description;

    /**
     * The URL of the product's image.
     */
    private String imageUrl;

    /**
     * The price of the product.
     */
    private double price;

    /**
     * The name of the category to which the product belongs.
     */
    private String category;

    /**
     * Converts a Product entity to a ProductResponseDto.
     *
     * @param product The Product entity to be converted.
     * @return A ProductResponseDto containing the product details, or null if the input is null.
     */
    public static ProductResponseDto fromEntity(Product product) {
        if (product == null) return null;
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setImageUrl(product.getImageUrl());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setCategory(product.getCategory().getName());
        return productResponseDto;
    }
}