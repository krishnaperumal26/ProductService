package com.kp.firstspringproject.dtos;

import com.kp.firstspringproject.models.Category;
import lombok.Data;

@Data
public class FakeStoreProductDto {
    private Long id;
    private String title;
    private String description;
    private double price;
    private String image;
    private String category;
}
