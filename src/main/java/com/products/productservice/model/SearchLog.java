package com.products.productservice.model;

import jakarta.persistence.Entity;
import lombok.Data;

/**
 * Entity class representing a Search Log.
 * Extends BaseModel to inherit common fields and functionality.
 * This class is used to log search queries performed by users.
 */
@Entity
@Data
public class SearchLog extends BaseModel {

    /**
     * The search query entered by the user.
     */
    private String query;

    /**
     * The page number of the search results.
     * Indicates which page of results the user is viewing.
     */
    private int pageNumber;

    /**
     * The number of results per page.
     * Specifies the size of the result set for each page.
     */
    private int pageSize;

    /**
     * The parameter used to sort the search results.
     * Example values could include "name", "price", or "date".
     */
    private String sortParam;
}