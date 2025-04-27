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
 */
@Data
@Entity
public class Category extends BaseModel implements Serializable {

    /**
     * A brief description of the category.
     */
    private String description;

    /**
     * The list of products associated with this category.
     * This relationship is mapped by the 'category' field in the Product entity.
     * Products are fetched eagerly using JOIN fetch mode.
     * Ignored during JSON serialization to prevent circular references.
     */
    @OneToMany(mappedBy = "category")
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private List<Product> products;
}