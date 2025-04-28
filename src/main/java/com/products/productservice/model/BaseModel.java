package com.products.productservice.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Base entity class providing common fields for all entities.
 * Marked as a MappedSuperclass to be inherited by other entity classes.
 * Implements Serializable for object serialization.
 */
@Data
@MappedSuperclass
public class BaseModel implements Serializable {

    /**
     * The unique identifier for the entity.
     * Auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The timestamp when the entity was created.
     * This field is typically set automatically when the entity is persisted.
     */
    private Date createdAt;

    /**
     * The timestamp when the entity was last modified.
     * This field is typically updated automatically when the entity is updated.
     */
    private Date lastModified;

    /**
     * Indicates whether the entity is marked as deleted.
     * This field is typically used for soft deletion.
     */
    private boolean isDeleted;

}