package com.products.productservice.dtos;

import com.products.productservice.model.Category;
import com.products.productservice.model.Product;
import com.products.productservice.model.Rating;
import lombok.Data;

@Data
public class ProductResponseDto {
    private long id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
//    private Rating rating;

    public ProductResponseDto fromEntity(Product product) {
        id = product.getId();
        title = product.getTitle();
        price = product.getPrice();
        description = product.getDescription();
        category = product.getCategory().getName();
        image = product.getImage();
//        rating = product.getRating();
        return this;
    }
}
