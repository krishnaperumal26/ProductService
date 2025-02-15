package com.products.productservice.services;

import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product getProductById(long id) throws ProductNotFoundException;
    List<Product> getAllProducts();
    Product createProduct(String title, double price, String description, String category, String image);
}
