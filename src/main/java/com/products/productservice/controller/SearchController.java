package com.products.productservice.controller;

import com.products.productservice.dtos.ProductResponseDto;
import com.products.productservice.model.Product;
import com.products.productservice.services.ISearchService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {

    // Service for handling search operations
    ISearchService searchService;

    /**
     * Constructor for SearchController.
     *
     * @param searchService The search service used to perform product searches.
     */
    public SearchController(ISearchService searchService) {
        this.searchService = searchService; // Initialize the search service
    }

    /**
     * Handles GET requests to perform a product search.
     *
     * @param query      The search query string.
     * @param pageNumber The page number for pagination.
     * @param pageSize   The number of items per page for pagination.
     * @param sortParam  The parameter by which the results should be sorted.
     * @return A list of ProductResponseDto containing the search results.
     */
    @GetMapping("/search")
    public List<ProductResponseDto> search(@RequestParam String query,
                                           @RequestParam int pageNumber,
                                           @RequestParam int pageSize,
                                           @RequestParam String sortParam) {
        // Perform the search using the search service
        Page<Product> productPage = searchService.search(query, pageNumber, pageSize, sortParam);

        // Initialize a list to hold the response DTOs
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        // Get the list of products from the search result
        List<Product> products = productPage.getContent();

        // Convert each Product entity to a ProductResponseDto
        for (Product product : products) {
            ProductResponseDto productResponseDto = ProductResponseDto.fromEntity(product);
            productResponseDtos.add(productResponseDto); // Add the DTO to the response list
        }

        // Return the list of ProductResponseDto
        return productResponseDtos;
    }
}