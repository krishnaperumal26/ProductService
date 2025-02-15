package com.products.productservice.services;

import com.products.productservice.dtos.FakeStoreProductDto;
import com.products.productservice.dtos.FakeStoreProductRequestDto;
import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements ProductService{

    RestTemplate restTemplate;
    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    String fakeStoreUrl = "https://fakestoreapi.com/products/";
    @Override
    public Product getProductById(long id) throws ProductNotFoundException {
        FakeStoreProductDto fakeStoreProductDto =
                restTemplate.getForObject(fakeStoreUrl + id, FakeStoreProductDto.class);
        if(fakeStoreProductDto==null)
        {
            throw new ProductNotFoundException("Product is not found for Id "+id);
        }
        Product product = fakeStoreProductDto.toProduct();
        return product;
    }

    @Override
    public List<Product> getAllProducts()
    {
        FakeStoreProductDto[] fakeStoreProductDto = restTemplate.getForObject
                (fakeStoreUrl, FakeStoreProductDto[].class);
        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto1 : fakeStoreProductDto)
        {
            products.add(fakeStoreProductDto1.toProduct());
        }
        return products;
    }

    @Override
    public Product createProduct(String title, double price, String description, String category, String image) {
       FakeStoreProductRequestDto fakeStoreProductRequestDto = new FakeStoreProductRequestDto();
        fakeStoreProductRequestDto.setTitle(title);
        fakeStoreProductRequestDto.setPrice(price);
        fakeStoreProductRequestDto.setDescription(description);
        fakeStoreProductRequestDto.setCategory(category);
        fakeStoreProductRequestDto.setImage(image);

        FakeStoreProductDto fakeStoreProductDto = restTemplate.postForObject(fakeStoreUrl, fakeStoreProductRequestDto, FakeStoreProductDto.class);


        return fakeStoreProductDto.toProduct();

    }
}
