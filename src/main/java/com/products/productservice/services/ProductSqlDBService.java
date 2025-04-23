package com.products.productservice.services;

import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Category;
import com.products.productservice.model.Product;
import com.products.productservice.repositories.CategoryRepository;
import com.products.productservice.repositories.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("productDBService")
@Primary
public class ProductSqlDBService implements ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public ProductSqlDBService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Product getProductById(long id) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty())
        {
            throw new ProductNotFoundException("Product is not found for Id "+id);
        }
        return optionalProduct.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String name, String description, double price,
                                 String imageUrl, String category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        Category categoryObj = getCategoryFromDB(category);
        product.setCategory(categoryObj);
        return productRepository.save(product);
    }

    private Category getCategoryFromDB(String name){
        Optional<Category> optioanlCategory = categoryRepository.findByName(name);
        if(optioanlCategory.isPresent())
        {
            return optioanlCategory.get();
        }
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    @Override
    public Product updateProduct(Long id, String title, String description, double price, String imageUrl, String category) throws ProductNotFoundException {
        return null;
    }

    @Override
    public String deleteProduct(Long id) {
        return "";
    }
}
