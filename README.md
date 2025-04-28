# ProductService
ProductService is a Spring Boot application that provides a RESTful API for managing products, categories, and AI-generated content.


# Features
Product management (CRUD operations)

Category management with eager loading of associated products

Advanced product search with pagination and sorting

Soft deletion of products

Integration with Azure OpenAI for AI chat completions

AI image generation with Azure OpenAI

Search query logging

Redis caching

# Technologies
Java

Spring Boot

Spring Data JPA

MySQL Database

Redis Cache

Azure OpenAI API

# Prerequisites
JDK 17 or later

Maven

MySQL Server

Redis Server

Azure OpenAI API keys (for AI features)

# Setup Instructions
Clone the repository

Configure the database in application.properties

Configure Redis in application.properties

Configure Azure OpenAI API keys in application.properties

Build the application: mvn clean install

Run the application: mvn spring-boot:run

# API Endpoints
## Products
GET /products - Get all products

GET /products/{id} - Get a product by ID

POST /products - Create a new product

PUT /products/{id} - Update a product

DELETE /products/{id} - Soft delete a product

## Categories
GET /categories - Get all categories

GET /categories/{name} - Get a category by name

POST /categories - Create a new category

## Search
GET /search?query={query}&pageNumber={pageNumber}&pageSize={pageSize}&sortParam={sortParam} - Search for products

# Data Models
The application uses several entities:


Product - Represents a product

Category - Represents a product category

SearchLog - Logs search operations

AIGenerationType - Types of AI generation

AIGenerationLog - Logs AI generation operations

All entities extend the BaseModel which provides common fields:


id
createdAt
lastModified
isDeleted

# AI Features
The application integrates with Azure OpenAI to provide:

*AI chat completions

*AI image generation for products

## Contributing
Contributions are welcome. Please feel free to submit a Pull Request.
