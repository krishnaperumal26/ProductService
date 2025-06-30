package com.products.productservice.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

/**
 * Configuration class for setting up Redis-based vector storage.
 * This class defines beans for creating and configuring Redis connections
 * and vector store instances for embedding-based operations.
 */
@Configuration
public class RedisVectorConfig {

    /**
     * Creates and configures a JedisPooled bean for connecting to Redis.
     *
     * @return A pooled Redis connection instance.
     */
    @Bean
    public JedisPooled jedisPooled() {
        return new JedisPooled("localhost", 6379);
    }

    /**
     * Creates and configures a RedisVectorStore bean for storing and retrieving embeddings.
     *
     * @param jedisPooled The Redis connection pool used for interacting with Redis.
     * @param embeddingModel The embedding model used for generating vector representations.
     * @return A configured instance of RedisVectorStore.
     */
    @Bean
    public VectorStore redisVectorStore(JedisPooled jedisPooled,
                                        @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName("product-recommendations") // Name of the index for storing vectors.
                .prefix("product:embedding:") // Prefix for keys in Redis.
                .metadataFields(
                        // Metadata fields for storing additional information about embeddings.
                        RedisVectorStore.MetadataField.tag("category"),
                        RedisVectorStore.MetadataField.numeric("price"),
                        RedisVectorStore.MetadataField.numeric("id")
                )
                .embeddingFieldName("embedding") // Field name for storing embedding data.
                .build();
    }
}