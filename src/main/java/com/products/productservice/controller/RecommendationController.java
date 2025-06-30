package com.products.productservice.controller;

import com.products.productservice.dtos.ProductResponseDto;
import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;
import com.products.productservice.services.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for handling product recommendations.
 * Provides endpoints for retrieving recommended products based on a given product ID.
 */
@RestController
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Constructor for RecommendationController.
     *
     * @param recommendationService The service used to fetch product recommendations.
     */
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Endpoint to retrieve product recommendations for a given product ID.
     *
     * @param productId The ID of the product for which recommendations are requested.
     * @return A list of ProductResponseDto objects containing recommended product details.
     * @throws ProductNotFoundException If the product with the given ID is not found.
     */
    @GetMapping("/recommendations/{productId}")
    public List<ProductResponseDto> getRecommendations(@PathVariable("productId") long productId) throws ProductNotFoundException {
        // Fetch hybrid recommendations from the service.
        List<Product> products = recommendationService.getHybridRecommendations(productId);

        // Convert Product entities to ProductResponseDto objects.
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for (Product product : products) {
            ProductResponseDto productResponseDto = ProductResponseDto.fromEntity(product);
            productResponseDtos.add(productResponseDto);
        }

        // Return the list of recommended products.
        return productResponseDtos;
    }
}