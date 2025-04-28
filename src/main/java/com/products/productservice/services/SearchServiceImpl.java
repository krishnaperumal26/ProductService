package com.products.productservice.services;

import com.products.productservice.model.Product;
import com.products.productservice.model.SearchLog;
import com.products.productservice.repositories.IAIGenerationTypeRepository;
import com.products.productservice.repositories.IProductRepository;
import com.products.productservice.repositories.ISearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Implementation of the SearchService interface for searching products.
 * Provides functionality to search for products with pagination, sorting, and logging of search queries.
 */
@Service
public class SearchServiceImpl implements ISearchService {

    private final IProductRepository productRepository; // Repository for accessing product data
    private final ISearchRepository searchRepository;   // Repository for logging search queries

    /**
     * Constructor for SearchServiceImpl.
     *
     * @param productRepository The repository used to access product data.
     * @param searchRepository  The repository used to log search queries.
     */
    public SearchServiceImpl(IProductRepository productRepository, ISearchRepository searchRepository) {
        this.productRepository = productRepository;
        this.searchRepository = searchRepository;
    }

    /**
     * Searches for products based on a query string, with pagination and sorting.
     * Logs the search query details to the database.
     *
     * @param query      The search query to filter products by name.
     * @param pageNumber The page number for pagination.
     * @param pageSize   The number of items per page.
     * @param sortParam  The parameter to sort the results by.
     * @return A paginated list of products matching the search query.
     */
    @Override
    public Page<Product> search(String query, int pageNumber, int pageSize, String sortParam) {
        // Define sorting: primary by sortParam in descending order, secondary by price in ascending order
        Sort sort = Sort.by(sortParam).descending().and(Sort.by("price").ascending());

        // Create a new SearchLog object to log the search query details
        SearchLog searchLog = new SearchLog();
        searchLog.setQuery(query); // Set the search query
        searchLog.setPageNumber(pageNumber); // Set the page number
        searchLog.setPageSize(pageSize); // Set the page size
        searchLog.setSortParam(sortParam); // Set the sorting parameter
        searchLog.setCreatedAt(new Date()); // Set the creation timestamp
        searchLog.setLastModified(searchLog.getCreatedAt()); // Set the last modified timestamp
        searchRepository.save(searchLog); // Save the search log to the database

        // Create a pageable object with the given page number and size
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Perform the search in the repository and return the results
        return productRepository.findByNameContaining(query, pageable);
    }
}