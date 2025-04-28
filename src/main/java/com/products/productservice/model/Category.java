package com.products.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.List;

/**
 * Entity class representing a Category.
 * Extends BaseModel and implements Serializable for persistence and serialization.
 * A Category is used to group related products together.
 */
@Data
@Entity
public class Category extends BaseModel implements Serializable {

    /**
     * The name of the category.
     * This field provides a brief description or title for the category.
     */
    private String name;

    /**
     * The list of products associated with this category.
     * This is a one-to-many relationship, where a category can have multiple products.
     * - Mapped by the 'category' field in the Product entity.
     * - Fetched eagerly using JOIN fetch mode to optimize performance.
     * - Ignored during JSON serialization to prevent circular references.
     */
    @OneToMany(mappedBy = "category")
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private List<Product> products;
}