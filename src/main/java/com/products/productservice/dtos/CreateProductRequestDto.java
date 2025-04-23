package com.products.productservice.dtos;

import lombok.Data;

@Data
public class CreateProductRequestDto {

    private String name;
    private String description;
    private String category;
    private double price;
    private String imageUrl;

}
