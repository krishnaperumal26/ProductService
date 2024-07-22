package com.kp.firstspringproject.services;

import com.kp.firstspringproject.dtos.FakeStoreProductDto;
import com.kp.firstspringproject.models.Category;
import com.kp.firstspringproject.models.Product;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
//@Repository
//@Controller
//@Component
public class FakStoreProductService implements ProductService{

    private RestTemplate restTemplate;
    FakStoreProductService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    private Product convertFakeStoreDtoToProduct(FakeStoreProductDto dto)
    {
        Product product = new Product();
        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setImage(dto.getImage());
        product.setDescription(dto.getDescription());

        Category category = new Category();
        category.setDescription(dto.getCategory());
        product.setCategory(category);

        return product;
    }

    @Override
    public Product getProductById(Long id) {
        //Call Fakestore API  to get product by id
        FakeStoreProductDto fakeStoreProductDto =restTemplate.getForObject("https://fakestoreapi.com/products/"+id, FakeStoreProductDto.class);
        //DTO can be used when their is structure mismatch. In model, Category is object but expected category type is string.
        //1st param - URL
        //2nd param - Response;

        if(fakeStoreProductDto==null)
            return null;

        //Convert DTO into product
        return convertFakeStoreDtoToProduct(fakeStoreProductDto);
    }

    @Override
    public List<Product> getAllProduct() {
        return List.of();
    }


//    Git command
    /*
    git init
    git add.
    git commit -m "Implemented3rd party API"
    git push -m origin main
    git remote add origin https://github.com/krishnaperumal26/ProductService.git
    git push -m origin main

     */



}
