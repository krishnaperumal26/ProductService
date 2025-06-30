package com.products.productservice.services;

import com.products.productservice.model.Product;
import com.products.productservice.repositories.IProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for synchronizing product data with Redis as documents.
 * This class interacts with the Redis vector store and product repository
 * to convert product data into document format and store it in Redis.
 */
@Service
public class ProductSyncRecommendationService {
    private final RedisVectorStore vectorStore;
    private final IProductRepository productRepository;

    /**
     * Constructor for ProductSyncRecommendationService.
     *
     * @param vectorStore The Redis vector store used for storing documents.
     * @param productRepository The repository for accessing product data.
     */
    public ProductSyncRecommendationService(RedisVectorStore vectorStore, IProductRepository productRepository) {
        this.vectorStore = vectorStore;
        this.productRepository = productRepository;
    }

    /**
     * Synchronizes all products from the database to Redis as documents.
     * Each product is converted into a document with metadata and added to the Redis vector store.
     */
    @PostConstruct
    @Scheduled(fixedRate = 7200000) // Runs every 2 hours
    public void syncProductsToRedisAsDocuments() {
        // Retrieve all products from the repository.
        List<Product> products = productRepository.findAll();

        // Initialize a list to hold documents.
        List<Document> documents = new ArrayList<>();

        // Iterate through each product and convert it to a document.
        for (Product product : products) {
            if (product.getDescription() != null) {
                // Create metadata for the document.
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("id", product.getId());
                metadata.put("category", product.getCategory().getName());
                metadata.put("price", product.getPrice());

                // Create a document with the product description and metadata.
                Document doc = new Document(product.getDescription(), metadata);

                // Add the document to the list.
                documents.add(doc);
            }
        }

        // Add all documents to the Redis vector store.
        vectorStore.add(documents);
    }
}