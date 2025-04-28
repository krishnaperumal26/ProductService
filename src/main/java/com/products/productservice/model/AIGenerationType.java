package com.products.productservice.model;

import jakarta.persistence.Entity;
import lombok.Data;

/**
 * Entity class representing the type of AI generation.
 * This class is used to define and categorize the types of AI content generation
 * (e.g., description generation, image generation) used in the system.
 */
@Entity
@Data
public class AIGenerationType extends BaseModel {

    /**
     * The name or identifier of the AI generation type.
     * Example values could include "Description Generation" or "Image Generation".
     */
    private String type;

}