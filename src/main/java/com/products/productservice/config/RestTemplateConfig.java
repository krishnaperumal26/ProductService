package com.products.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for defining application-wide beans.
 * Provides a centralized location for Spring-managed bean definitions.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates and configures a {@link RestTemplate} bean.
     * The {@link RestTemplate} is used for making HTTP requests to external services.
     *
     * @return A new instance of {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Return a new RestTemplate instance
    }
}