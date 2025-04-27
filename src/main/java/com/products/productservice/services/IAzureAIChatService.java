package com.products.productservice.services;

/**
 * Interface for Azure AI Chat Service.
 * This interface defines a method for generating AI-based responses using system and user prompts.
 */
public interface IAzureAIChatService {

    /**
     * Generates a response from Azure AI Chat service based on the provided prompts.
     *
     * @param systemPrompt The system-level prompt to guide the AI's behavior.
     * @param userPrompt   The user-level prompt to specify the query or task.
     * @return The generated response from Azure AI Chat service.
     */
    String GenerateResponse(String systemPrompt, String userPrompt);
}