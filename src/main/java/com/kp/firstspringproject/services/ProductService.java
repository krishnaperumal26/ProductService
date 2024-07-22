package com.kp.firstspringproject.services;

import com.kp.firstspringproject.models.Product;
import java.util.List;
import java.util.*;
public interface ProductService {

    public Product getProductById(Long id);

    public List<Product> getAllProduct();

    public Product replaceProduct(Long id, Product product);
}
