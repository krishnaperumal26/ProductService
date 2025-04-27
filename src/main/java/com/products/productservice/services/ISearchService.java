package com.products.productservice.services;

import com.products.productservice.model.Product;
import org.springframework.data.domain.Page;

/**
 * Interface for defining search-related operations for products.
 */
public interface ISearchService {

    /**
     * Searches for products based on a query string, with pagination and sorting.
     *
     * @param query      The search query to filter products by name.
     * @param pageNumber The page number for pagination.
     * @param pageSize   The number of items per page.
     * @param sortParam  The parameter to sort the results by.
     * @return A paginated list of products matching the search query.
     */
    Page<Product> search(String query, int pageNumber, int pageSize, String sortParam);
}