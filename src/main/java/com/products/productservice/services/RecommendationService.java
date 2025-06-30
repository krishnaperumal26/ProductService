package com.products.productservice.services;

import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;
import com.products.productservice.repositories.IProductRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class for providing product recommendations.
 * Combines query-based and AI-based approaches to generate hybrid recommendations.
 */
@Service
public class RecommendationService {
    private final RedisVectorStore vectorStore; // Redis vector store for AI-based recommendations.
    private final IProductRepository productRepository; // Repository for accessing product data.

    /**
     * Constructor for RecommendationService.
     *
     * @param vectorStore The Redis vector store used for AI-based recommendations.
     * @param productRepository The repository for accessing product data.
     */
    RecommendationService(RedisVectorStore vectorStore, IProductRepository productRepository) {
        this.vectorStore = vectorStore;
        this.productRepository = productRepository;
    }

    /**
     * Retrieves hybrid recommendations for a given product ID.
     * Combines query-based and AI-based recommendations to provide a comprehensive list.
     *
     * @param productId The ID of the product for which recommendations are requested.
     * @return A list of recommended products.
     * @throws ProductNotFoundException If the product with the given ID is not found.
     */
    public List<Product> getHybridRecommendations(long productId) throws ProductNotFoundException {
        // Fetch the target product from the repository or throw an exception if not found.
        Product targetProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        // Use a LinkedHashSet to combine results from query-based and AI-based recommendations.
        Set<Product> results = new LinkedHashSet<>();
        results.addAll(getQueryBasedRecommendedProducts(targetProduct)); // Add query-based recommendations.
        results.addAll(getAIRecommendedProducts(targetProduct)); // Add AI-based recommendations.

        // Convert the set to a list and return.
        return new ArrayList<>(results);
    }

    /**
     * Retrieves query-based recommendations for a given product.
     * Finds similar products within the same category based on price range.
     *
     * @param targetProduct The product for which recommendations are requested.
     * @return A set of products that match the query-based criteria.
     */
    public Set<Product> getQueryBasedRecommendedProducts(Product targetProduct) {
        // Initialize a LinkedHashSet to store query-based results.
        Set<Product> results = new LinkedHashSet<>();

        // Fetch similar products from the repository based on category and price range.
        List<Product> repoResults = productRepository.findSimilarInCategory(targetProduct.getCategory(),
                targetProduct.getPrice() * 0.7, // Minimum price (70% of target product price).
                targetProduct.getPrice() * 1.3, // Maximum price (130% of target product price).
                targetProduct.getId()); // Exclude the target product ID.

        // Add the fetched products to the results set.
        results.addAll(repoResults);
        return results;
    }

    /**
     * Retrieves AI-based recommendations for a given product.
     * Uses the product description to perform a similarity search in the Redis vector store.
     *
     * @param targetProduct The product for which recommendations are requested.
     * @return A set of products that match the AI-based criteria.
     */
    public Set<Product> getAIRecommendedProducts(Product targetProduct) {
        // Initialize a LinkedHashSet to store AI-based results.
        Set<Product> results = new LinkedHashSet<>();

        // Build a search request using the product description and specify the top K results.
        SearchRequest searchRequest = SearchRequest.builder()
                .query(targetProduct.getDescription()) // Query based on product description.
                .topK(5) // Retrieve the top 5 similar results.
                .build();

        // Perform a similarity search in the Redis vector store.
        List<Document> aiResults = vectorStore.similaritySearch(searchRequest);

        // Iterate through the search results and fetch product details from the repository.
        for (Document doc : aiResults) {
            Object idObj = doc.getMetadata().get("id"); // Extract the product ID from metadata.

            if (idObj == null) {
                continue; // Skip if the ID is missing.
            }
            try {
                // Parse the ID and fetch the product from the repository.
                Long docProductId = Long.parseLong(idObj.toString());
                productRepository.findById(docProductId).ifPresent(results::add);
            } catch (NumberFormatException e) {
                // Handle invalid ID format in document metadata.
            }
        }
        return results;
    }
}