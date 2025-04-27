package com.products.productservice.services;

import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Category;
import com.products.productservice.model.Product;
import com.products.productservice.repositories.ICategoryRepository;
import com.products.productservice.repositories.IProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing products in a SQL database.
 * This service provides methods for CRUD operations on products and integrates with Azure AI for generating product images and descriptions.
 */
@Service("productDBService")
@Primary
public class ProductSqlDBService implements IProductService {
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final AzureAIProductServiceImpl azureAIProductService;

    /**
     * Constructor for ProductSqlDBService.
     *
     * @param productRepository    Repository for managing product entities.
     * @param categoryRepository   Repository for managing category entities.
     * @param azureAIProductService Service for generating AI-based content.
     */
    public ProductSqlDBService(IProductRepository productRepository, ICategoryRepository categoryRepository, AzureAIProductServiceImpl azureAIProductService) {
        this.azureAIProductService = azureAIProductService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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
     *
     * @param name        The name of the product.
     * @param description The description of the product.
     * @param price       The price of the product.
     * @param imageUrl    The image URL of the product.
     * @param category    The category of the product.
     * @return The created product.
     */
    @Override
    public Product createProduct(String name, String description, double price, String imageUrl, String category) {
        // Create a new product instance
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        // Retrieve or create the category from the database
        Category categoryObj = getCategoryFromDB(category);
        product.setCategory(categoryObj);
        System.out.println("Category is : " + categoryObj.getName());

        // Generate image URL if not provided
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = GenerateImageFromAIBasedOnProduct(product);
        }
        product.setImageUrl(imageUrl);

        // Generate description if not provided
        if (description == null || description.isEmpty()) {
            description = GenerateDescriptionFromAIBasedOnProduct(product);
        }
        product.setDescription(description);

        // Save the product to the repository
        return productRepository.save(product);
    }

    /**
     * Retrieves a category by its name from the database.
     * If the category does not exist, it is created.
     *
     * @param name The name of the category.
     * @return The category object.
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
        return categoryRepository.save(category);
    }

    /**
     * Generates an image URL for a product using Azure AI.
     *
     * @param product The product for which the image is generated.
     * @return The generated image URL.
     */
    public String GenerateImageFromAIBasedOnProduct(Product product) {
        // Create a prompt for Azure AI to generate an image
        String prompt = "Generate image for " + product.getCategory().getName() + " product named " + product.getName() + ". Add small text 'AI Generated Image' in the Image bottom";
        return azureAIProductService.GenerateImage(prompt, product.getName() + "_" + System.currentTimeMillis());
    }

    /**
     * Generates a description for a product using Azure AI.
     *
     * @param product The product for which the description is generated.
     * @return The generated description.
     */
    public String GenerateDescriptionFromAIBasedOnProduct(Product product) {
        // Create prompts for Azure AI to generate a description
        String systemPrompt = "You are product Content writer to give product very short description in 300 to 400 characters";
        String userPrompt = "Generate 300 to 400 characters professional marketing description for a " + product.getCategory().getName() + " product named " + product.getName() + ". Focus on benefits and unique selling points. Avoid technical jargon. Use markdown formatting.";
        return azureAIProductService.GenerateResponse(systemPrompt, userPrompt);
    }

    /**
     * Updates an existing product or creates a new one if it does not exist.
     *
     * @param id          The ID of the product to update.
     * @param name        The new name of the product.
     * @param description The new description of the product.
     * @param price       The new price of the product.
     * @param imageUrl    The new image URL of the product.
     * @param category    The new category of the product.
     * @return The updated or newly created product.
     */
    @Override
    public Product updateProduct(Long id, String name, String description, double price, String imageUrl, String category) {
        // Check if the product exists in the repository
        Optional<Product> optionalProduct = productRepository.findById(id);

        // Create a new product instance
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        // Retrieve or create the category from the database
        Category categoryObj = getCategoryFromDB(category);
        product.setCategory(categoryObj);

        // Generate image URL if not provided
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = GenerateImageFromAIBasedOnProduct(product);
        }
        // Generate description if not provided
        if (description == null || description.isEmpty()) {
            description = GenerateDescriptionFromAIBasedOnProduct(product);
        }
        product.setImageUrl(imageUrl);
        product.setDescription(description);

        // Update the existing product if found
        if (optionalProduct.isPresent()) {
            Product productToUpdate = optionalProduct.get();
            productToUpdate.setName(name);
            productToUpdate.setDescription(description);
            productToUpdate.setPrice(price);
            productToUpdate.setImageUrl(imageUrl);
            productToUpdate.setCategory(categoryObj);
            return productRepository.save(productToUpdate);
        }
        // Save the new product if it does not exist
        return productRepository.save(product);
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
            productRepository.deleteById(id);
            return "Product deleted successfully";
        } else {
            // Return a message if the product is not found
            return "Product not found";
        }
    }
}