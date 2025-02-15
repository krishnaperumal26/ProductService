package com.products.productservice.dtos;

import lombok.Data;

@Data
public class CreateFakeStoreProductDto {

    private String title;
    private double price;
    private String description;
    private String category;
    private String image;

}
