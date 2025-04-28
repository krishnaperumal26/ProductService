package com.products.productservice.services;

import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.AIGenerationLog;
import com.products.productservice.model.AIGenerationType;
import com.products.productservice.model.Category;
import com.products.productservice.model.Product;
import com.products.productservice.repositories.IAIGenerationLogRepository;
import com.products.productservice.repositories.IAIGenerationTypeRepository;
import com.products.productservice.repositories.ICategoryRepository;
import com.products.productservice.repositories.IProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing products in a SQL database.
 * This service provides methods for CRUD operations on products and integrates with Azure AI for generating product images and descriptions.
 * Marked as the primary implementation of the IProductService interface to be autowired by default.
 */
@Service("productDBService")
@Primary
public class ProductSqlDBService implements IProductService {
    private final IProductRepository productRepository;            // Repository for product data access
    private final ICategoryRepository categoryRepository;          // Repository for category data access
    private final AzureAIProductServiceImpl azureAIProductService; // Service for AI content generation
    private final IAIGenerationTypeRepository aiGenerationTypeRepository; // Repository for AI generation types
    private final IAIGenerationLogRepository aiGenerationLogRepository;   // Repository for AI generation logs

    /**
     * Constructor for ProductSqlDBService.
     * Uses constructor injection to provide all required dependencies.
     *
     * @param productRepository         Repository for managing product entities.
     * @param categoryRepository        Repository for managing category entities.
     * @param azureAIProductService     Service for generating AI-based content.
     * @param aiGenerationTypeRepository Repository for managing AI generation type entities.
     * @param aiGenerationLogRepository Repository for managing AI generation log entities.
     */
    public ProductSqlDBService(IProductRepository productRepository,
                               ICategoryRepository categoryRepository,
                               AzureAIProductServiceImpl azureAIProductService,
                               IAIGenerationTypeRepository aiGenerationTypeRepository,
                               IAIGenerationLogRepository aiGenerationLogRepository) {
        this.azureAIProductService = azureAIProductService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.aiGenerationTypeRepository = aiGenerationTypeRepository;
        this.aiGenerationLogRepository = aiGenerationLogRepository;
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The product with the specified ID.
     * @throws ProductNotFoundException If no product is found with the given ID.
     */
    @Override
    public Product getProductById(long id) throws ProductNotFoundException {
        // Fetch the product by ID from the repository
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            // Throw an exception if the product is not found
            throw new ProductNotFoundException("Product is not found for Id " + id);
        }
        return optionalProduct.get();
    }

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all products.
     */
    @Override
    public List<Product> getAllProducts() {
        // Fetch all products from the repository
        return productRepository.findAll();
    }

    /**
     * Creates a new product with the specified details.
     * If the image URL or description is not provided, they are generated using Azure AI.
     * Also logs the AI generation activities to the database.
     *
     * @param name        The name of the product.
     * @param description The description of the product (can be null/empty to trigger AI generation).
     * @param price       The price of the product.
     * @param imageUrl    The image URL of the product (can be null/empty to trigger AI generation).
     * @param category    The category of the product.
     * @return The created product with all fields populated.
     */
    @Override
    public Product createProduct(String name, String description, double price, String imageUrl, String category) {
        // Create a new product instance with basic properties
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCreatedAt(new Date());
        product.setLastModified(product.getCreatedAt());

        // Retrieve or create the category from the database
        Category categoryObj = getCategoryFromDB(category);
        product.setCategory(categoryObj);
        System.out.println("Category is : " + categoryObj.getName());

        // Initialize log objects for potential AI generations
        AIGenerationLog aiGenerationImageLog = null;
        AIGenerationLog aiGenerationDescriptionLog = null;

        // Generate image URL if not provided
        if (imageUrl == null || imageUrl.isEmpty()) {
            aiGenerationImageLog = new AIGenerationLog(); // Create log entry for image generation
            imageUrl = GenerateImageFromAIBasedOnProduct(product, aiGenerationImageLog);
        }
        product.setImageUrl(imageUrl);

        // Generate description if not provided
        if (description == null || description.isEmpty()) {
            aiGenerationDescriptionLog = new AIGenerationLog(); // Create log entry for description generation
            description = GenerateDescriptionFromAIBasedOnProduct(product, aiGenerationDescriptionLog);
        }
        product.setDescription(description);

        // Save the product to get its generated ID
        Product productAfterSave = productRepository.save(product);
        System.out.println("Product after save : " + productAfterSave.getId());

        // Update AI generation logs with the product ID and save them to the database
        if(aiGenerationImageLog != null) {
            aiGenerationImageLog.setProductId(productAfterSave.getId());
            aiGenerationLogRepository.save(aiGenerationImageLog);
        }
        if(aiGenerationDescriptionLog != null) {
            aiGenerationDescriptionLog.setProductId(productAfterSave.getId());
            aiGenerationLogRepository.save(aiGenerationDescriptionLog);
        }
        return productAfterSave;
    }

    /**
     * Retrieves a category by its name from the database.
     * If the category does not exist, it is created.
     *
     * @param name The name of the category to retrieve or create.
     * @return The existing or newly created category object.
     */
    private Category getCategoryFromDB(String name) {
        // Fetch the category by name from the repository
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }

        // Create and save a new category if it does not exist
        Category category = new Category();
        category.setName(name);
        category.setCreatedAt(new Date());
        category.setLastModified(category.getCreatedAt());
        return categoryRepository.save(category);
    }

    /**
     * Generates an image URL for a product using Azure AI.
     * Populates the provided AIGenerationLog object with details of the generation process.
     *
     * @param product The product for which the image is generated.
     * @param aiGenerationLog The log object to record the generation details.
     * @return The generated image URL or file path.
     */
    public String GenerateImageFromAIBasedOnProduct(Product product, AIGenerationLog aiGenerationLog) {
        // Create a prompt for Azure AI to generate an image based on product details
        String prompt = "Generate image for " + product.getCategory().getName() + " product named " + product.getName() + ". Add small text 'AI Generated Image' in the Image bottom";

        // Call Azure AI service to generate the image
        String response = azureAIProductService.GenerateImage(prompt, product.getName() + "_" + System.currentTimeMillis());

        // Log the AI generation details
        aiGenerationLog.setInputPrompt(prompt);
        AIGenerationType aiGenerationType = getAIGenerationTypeFromDB("Image");
        aiGenerationLog.setType(aiGenerationType);
        aiGenerationLog.setOutputResponse(response);
        aiGenerationLog.setLastModified(new Date());
        aiGenerationLog.setCreatedAt(new Date());
        return response;
    }

    /**
     * Generates a description for a product using Azure AI.
     * Populates the provided AIGenerationLog object with details of the generation process.
     *
     * @param product The product for which the description is generated.
     * @param aiGenerationLog The log object to record the generation details.
     * @return The generated product description.
     */
    public String GenerateDescriptionFromAIBasedOnProduct(Product product, AIGenerationLog aiGenerationLog) {
        // Create system and user prompts for Azure AI to generate a product description
        String systemPrompt = "You are product Content writer to give product very short description in 300 to 400 characters";
        String userPrompt = "Generate 300 to 400 characters professional marketing description for a " + product.getCategory().getName() +
                " product named " + product.getName() + ". Focus on benefits and unique selling points. Avoid technical jargon. Use markdown formatting.";

        // Call Azure AI service to generate the description
        String response = azureAIProductService.GenerateResponse(systemPrompt, userPrompt);

        // Log the AI generation details
        String prompt = "SystemPrompt: " + systemPrompt + "; UserPrompt: " + userPrompt;
        aiGenerationLog.setInputPrompt(prompt);
        AIGenerationType aiGenerationType = getAIGenerationTypeFromDB("Description");
        aiGenerationLog.setType(aiGenerationType);
        aiGenerationLog.setOutputResponse(response);
        aiGenerationLog.setLastModified(new Date());
        aiGenerationLog.setCreatedAt(new Date());
        return response;
    }

    /**
     * Retrieves an AI generation type by its name from the database.
     * If the type does not exist, it is created.
     *
     * @param type The name of the AI generation type to retrieve or create.
     * @return The existing or newly created AIGenerationType object.
     */
    private AIGenerationType getAIGenerationTypeFromDB(String type) {
        // Fetch the AI generation type by name from the repository
        Optional<AIGenerationType> optionalAIGenerationType = aiGenerationTypeRepository.findByType(type);
        if (optionalAIGenerationType.isPresent()) {
            return optionalAIGenerationType.get();
        }

        // Create and save a new AI generation type if it does not exist
        AIGenerationType aiGenerationType = new AIGenerationType();
        aiGenerationType.setType(type);
        aiGenerationType.setCreatedAt(new Date());
        aiGenerationType.setLastModified(aiGenerationType.getCreatedAt());
        return aiGenerationTypeRepository.save(aiGenerationType);
    }

    /**
     * Updates an existing product or creates a new one if it does not exist.
     * If the image URL or description is not provided, they are generated using Azure AI.
     * Also logs the AI generation activities to the database.
     *
     * @param id          The ID of the product to update.
     * @param name        The new name of the product.
     * @param description The new description of the product (can be null/empty to trigger AI generation).
     * @param price       The new price of the product.
     * @param imageUrl    The new image URL of the product (can be null/empty to trigger AI generation).
     * @param category    The new category of the product.
     * @return The updated or newly created product.
     */
    @Override
    public Product updateProduct(Long id, String name, String description, double price, String imageUrl, String category) {
        // Check if the product exists in the repository
        Optional<Product> optionalProduct = productRepository.findById(id);

        // Initialize a product object - either new or existing
        Product product = new Product();

        // Initialize log objects for potential AI generations
        AIGenerationLog aiGenerationImageLog = null;
        AIGenerationLog aiGenerationChatLog = null;

        // Setup basic product properties
        product.setName(name);
        product.setPrice(price);
        product.setCreatedAt(new Date());
        product.setLastModified(product.getCreatedAt());

        // Retrieve or create the category from the database
        Category categoryObj = getCategoryFromDB(category);
        product.setCategory(categoryObj);

        // Generate image URL if not provided
        if (imageUrl == null || imageUrl.isEmpty()) {
            aiGenerationImageLog = new AIGenerationLog();
            imageUrl = GenerateImageFromAIBasedOnProduct(product, aiGenerationImageLog);
        }

        // Generate description if not provided
        if (description == null || description.isEmpty()) {
            aiGenerationChatLog = new AIGenerationLog();
            description = GenerateDescriptionFromAIBasedOnProduct(product, aiGenerationChatLog);
        }
        product.setImageUrl(imageUrl);
        product.setDescription(description);

        Product productAfterSave;

        // Update the existing product if found
        if (optionalProduct.isPresent()) {
            Product productToUpdate = optionalProduct.get();
            productToUpdate.setName(name);
            productToUpdate.setDescription(description);
            productToUpdate.setPrice(price);
            productToUpdate.setImageUrl(imageUrl);
            productToUpdate.setCategory(categoryObj);
            productToUpdate.setLastModified(new Date());

            // Save AI generation logs with product ID
            if(aiGenerationImageLog != null) {
                aiGenerationImageLog.setProductId(productToUpdate.getId());
                aiGenerationLogRepository.save(aiGenerationImageLog);
            }
            if(aiGenerationChatLog != null) {
                aiGenerationChatLog.setProductId(productToUpdate.getId());
                aiGenerationLogRepository.save(aiGenerationChatLog);
            }
            productAfterSave = productRepository.save(productToUpdate);
        } else {
            // Save as new product if it doesn't exist
            productAfterSave = productRepository.save(product);

            // Save AI generation logs with new product ID
            if(aiGenerationImageLog != null) {
                aiGenerationImageLog.setProductId(productAfterSave.getId());
                aiGenerationLogRepository.save(aiGenerationImageLog);
            }
            if(aiGenerationChatLog != null) {
                aiGenerationChatLog.setProductId(productAfterSave.getId());
                aiGenerationLogRepository.save(aiGenerationChatLog);
            }
        }
        return productAfterSave;
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @return A message indicating whether the product was successfully deleted or not found.
     */
    @Override
    public String deleteProduct(Long id) {
        // Check if the product exists in the repository
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            // Delete the product if found
            productRepository.softDeleteById(id);
            return "Product deleted successfully";
        } else {
            // Return a message if the product is not found
            return "Product not found";
        }
    }
}