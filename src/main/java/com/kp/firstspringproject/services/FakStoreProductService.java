package com.kp.firstspringproject.services;

import com.kp.firstspringproject.dtos.FakeStoreProductDto;
import com.kp.firstspringproject.models.Category;
import com.kp.firstspringproject.models.Product;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

        FakeStoreProductDto[] facFakeStoreProductDtos   = restTemplate.getForObject("https://fakestoreapi.com/products/",FakeStoreProductDto[].class);

        List<Product> response = new ArrayList<>();
        for(FakeStoreProductDto facFakeStoreProductDto:facFakeStoreProductDtos)
        {
            response.add(convertFakeStoreDtoToProduct(facFakeStoreProductDto));
        }

        return response;
    }

    @Override
    public Product replaceProduct(Long id, Product product)
    {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setImage(product.getImage());
        fakeStoreProductDto.setDescription(product.getDescription());

        RequestCallback requestCallback = restTemplate.httpEntityCallback(fakeStoreProductDto, FakeStoreProductDto.class);
        HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor = new HttpMessageConverterExtractor(FakeStoreProductDto.class, restTemplate.getMessageConverters());
        FakeStoreProductDto response =  restTemplate.execute("https://fakestoreapi.com/products/"+id, HttpMethod.PUT, requestCallback, responseExtractor);



        return convertFakeStoreDtoToProduct(response);
    }

//    Git command
    /*
    git init
    git add.
    git commit -m "Implemented3rd party API"
    git push -m origin main (git branch -M main

    git remote add origin https://github.com/krishnaperumal26/ProductService.git
    git push -u origin main


Command from GIt
FOr New:
echo "# ProductService" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/krishnaperumal26/ProductService.git
git push -u origin main


for existing repo form cmd
git remote add origin https://github.com/krishnaperumal26/ProductService.git
git branch -M main
git push -u origin main

     */



}
