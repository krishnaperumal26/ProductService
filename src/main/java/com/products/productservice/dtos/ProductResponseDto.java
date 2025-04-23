package com.products.productservice.dtos;

import com.products.productservice.model.Product;
import lombok.Data;

@Data
public class ProductResponseDto {
    private long id;
    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private String category;


    public static ProductResponseDto fromEntity(Product product) {
        if(product == null) return null;
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
