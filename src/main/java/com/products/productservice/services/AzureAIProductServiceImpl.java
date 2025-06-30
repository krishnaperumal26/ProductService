package com.products.productservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.products.productservice.model.AIGenerationType;
import com.products.productservice.model.Category;
import com.products.productservice.repositories.IAIGenerationLogRepository;
import com.products.productservice.repositories.IAIGenerationTypeRepository;
import com.products.productservice.utils.HttpUrlConfig;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * Service implementation for interacting with Azure AI services.
 * This class provides methods to generate AI-based responses and images.
 * Implements both IAzureAIImageService and IAzureAIChatService interfaces.
 * Uses Spring's lazy initialization to create this bean only when needed.
 */
@Service
@Lazy
@NoArgsConstructor
@AllArgsConstructor
public class AzureAIProductServiceImpl implements IAIImageService, IAIChatService {

    /**
     * API key for Azure Chat service.
     * Retrieved from application properties.
     */
    @Value("${azure.openai.api-key.AiChatKey}")
    private String azureChatApiKey;

    /**
     * API key for Azure Image service.
     * Retrieved from application properties.
     */
    @Value("${azure.openai.api-key.AiImageKey}")
    private String azureImageApiKey;

    /**
     * Path to save generated product images.
     * Configured in application properties.
     */
    @Value("${product.image.path}")
    private String productImagePath;

    /**
     * Endpoint URL for Azure Chat service.
     * Configured in application properties.
     */
    @Value("${azure.openai.endpoint.AiChatURL}")
    private String azureChatUrl;

    /**
     * Endpoint URL for Azure Image service.
     * Configured in application properties.
     */
    @Value("${azure.openai.endpoint.AiImageURL}")
    private String azureImageUrl;

    /**
     * Generates a response from Azure AI Chat service based on the provided prompts.
     *
     * @param systemPrompt The system-level prompt to guide the AI's behavior.
     * @param userPrompt   The user-level prompt to specify the query or task.
     * @return The generated response from Azure AI Chat service, or null if an error occurs.
     */
    @Override
    public String GenerateResponse(String systemPrompt, String userPrompt) {
        // Construct the request body for the Azure Chat API
        String requestBody = "{\n" +
                "  \"messages\":[\n" +
                "\t{\n" +
                "\t\t\"role\": \"system\",\n" +
                "\t\t\"content\": \"" + systemPrompt + "\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"role\": \"user\",\n" +
                "\t\t\"content\": \"" + userPrompt + "\"\n" +
                "\t}\n" +
                "],\n" +
                "  \"frequency_penalty\": 0,\n" +
                "  \"presence_penalty\": 0,\n" +
                "  \"max_completion_tokens\": 800,\n" +
                "  \"stop\": null\n" +
                "}";
        try {
            // Send the POST request and parse the response
            String responseBody = HttpUrlConfig.sendPostRequest(azureChatUrl, azureChatApiKey, requestBody);
            if (responseBody != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                // Extract the content of the response from the JSON path
                return rootNode.at("/choices/0/message/content").asText();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }
        return null; // Return null if an error occurs or no response received
    }

    /**
     * Generates an image using Azure AI Image service based on the provided prompt.
     *
     * @param prompt    The prompt describing the image to generate.
     * @param imageName The name to save the generated image file.
     * @return The file path of the saved image, or null if an error occurs.
     */
    @Override
    public String GenerateImage(String prompt, String imageName) {
        // Construct the request body for the Azure Image API
        String requestBody = "{\n" +
                "    \"prompt\": \"" + prompt + "\",\n" +
                "    \"n\": 1,\n" +
                "    \"size\": \"1024x1024\"\n" +
                "}";
        try {
            // Send the POST request and parse the response
            String responseBody = HttpUrlConfig.sendPostRequest(azureImageUrl, azureImageApiKey, requestBody);
            if (responseBody != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                // Extract the image URL from the response JSON
                String imageUrl = rootNode.at("/data/0/url").asText();
                // Define the output file path with timestamp to ensure uniqueness
                String outputFilePath = productImagePath + "\\" + imageName + "_" + System.currentTimeMillis() + ".png";
                try (BufferedInputStream inputStream = new BufferedInputStream(new URL(imageUrl).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {
                    // Download and save the image in chunks
                    byte[] dataBuffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }
                    return outputFilePath; // Return the file path of the saved image
                } catch (IOException e) {
                    System.err.println("Failed to download the image: " + e.getMessage()); // Log specific download error
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log any other exceptions
        }
        return null; // Return null if any step fails
    }
}