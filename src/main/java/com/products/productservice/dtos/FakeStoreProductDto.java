package com.products.productservice.dtos;

import com.products.productservice.model.Category;
import com.products.productservice.model.Product;
import com.products.productservice.model.Rating;
import lombok.Data;

@Data
public class FakeStoreProductDto {
    private long id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
//    private Rating rating;

    public Product toProduct()
    {
        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        product.setImage(image);
//        product.setRating(rating);

        Category categoryObj = new Category();
        categoryObj.setName(category);

        product.setCategory(categoryObj);
        return product;
    }
}

