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
     * The name of the product.
     * This field provides a brief title or identifier for the product.
     */
    private String name;

    /**
     * A detailed description of the product.
     * Limited to 10,000 characters.
     */
    @Column(length = 10000)
    private String description;

    /**
     * The URL of the product's image.
     * This field stores the location of the product's image resource.
     */
    private String imageUrl;

    /**
     * The price of the product.
     * This field represents the cost of the product in the system's currency.
     */
    private double price;

    /**
     * The category to which the product belongs.
     * Many products can belong to one category.
     * This is a many-to-one relationship with the Category entity.
     */
    @ManyToOne
    private Category category;
}