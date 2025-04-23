package com.products.productservice.services;

import com.products.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


public interface SearchService {
    Page<Product> search(String query, int pageNumber, int pageSize, String sortParam);

}
