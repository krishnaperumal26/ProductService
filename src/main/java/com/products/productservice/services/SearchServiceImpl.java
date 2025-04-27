package com.products.productservice.services;

import com.products.productservice.model.Product;
import com.products.productservice.repositories.IProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Implementation of the SearchService interface for searching products.
 */
@Service
public class SearchServiceImpl implements ISearchService {

    private final IProductRepository productRepository;

    /**
     * Constructor for SearchServiceImpl.
     *
     * @param productRepository The repository used to access product data.
     */
    public SearchServiceImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Searches for products based on a query string, with pagination and sorting.
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

        // Create a pageable object with the given page number and size
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Perform the search in the repository and return the results
        return productRepository.findByNameContaining(query, pageable);
    }
}