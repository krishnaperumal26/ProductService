package com.products.productservice.services;

/**
 * Interface for Azure AI Image Service.
 * This interface defines a method for generating images using Azure AI services.
 */
public interface IAIImageService {

    /**
     * Generates an image using Azure AI Image service based on the provided prompt.
     *
     * @param prompt    The prompt describing the image to generate.
     * @param imageName The name to save the generated image file.
     * @return The file path of the saved image.
     */
    String GenerateImage(String prompt, String imageName);
}