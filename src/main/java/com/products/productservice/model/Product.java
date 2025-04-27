package com.products.productservice.model;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

/**
 * Entity class representing a Product.
 * Extends BaseModel and implements Serializable for persistence and serialization.
 */
@Data
@Entity
public class Product extends BaseModel implements Serializable {

    /**
     * A detailed description of the product.
     * Limited to 10,000 characters.
     */
    @Column(length = 10000)
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
     * The category to which the product belongs.
     * Many products can belong to one category.
     */
    @ManyToOne
    private Category category;
}