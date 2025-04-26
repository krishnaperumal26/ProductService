package com.products.productservice.services;

import com.products.productservice.dtos.FakeStoreProductDto;
import com.products.productservice.dtos.FakeStoreProductRequestDto;
import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
@Primary
public class FakeStoreProductService implements ProductService{

    RestTemplate restTemplate;
    RedisTemplate<String, Object> redisTemplate;

    public FakeStoreProductService(RestTemplate restTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }
    String fakeStoreUrl = "https://fakestoreapi.com/products/";
    @Override
    public Product getProductById(long id) throws ProductNotFoundException {
        Product productFromCache = (Product)redisTemplate.opsForHash().get("PRODUCTMAP","PRODUCT_"+id);
//        Product productFromCache = (Product)redisTemplate.opsForValue().get(String.valueOf(id));
        if(productFromCache!=null)
        {
            return productFromCache;
        }
        FakeStoreProductDto fakeStoreProductDto =
                   restTemplate.getForObject(fakeStoreUrl + id, FakeStoreProductDto.class);
        if(fakeStoreProductDto==null)
        {
            throw new ProductNotFoundException("Product is not found for Id "+id);
        }
        Product productFromFakeStore  = fakeStoreProductDto.toProduct();
//        redisTemplate.opsForValue().set(String.valueOf(id), productFromFakeStore);
        redisTemplate.opsForHash().put("PRODUCTMAP","PRODUCT_"+id,productFromFakeStore);
        return productFromFakeStore;
    }

    @Override
    public List<Product> getAllProducts()
    {
        FakeStoreProductDto[] fakeStoreProductDtos = restTemplate.getForObject
                (fakeStoreUrl, FakeStoreProductDto[].class);
        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos)
        {
            products.add(fakeStoreProductDto.toProduct());
        }
        return products;
    }

    @Override
    public Product createProduct(String name,
                                 String description, double price,
                                 String imageUrl, String category) {
       FakeStoreProductRequestDto fakeStoreProductRequestDto = new FakeStoreProductRequestDto();
        fakeStoreProductRequestDto.setTitle(name);
        fakeStoreProductRequestDto.setDescription(description);
        fakeStoreProductRequestDto.setPrice(price);
        fakeStoreProductRequestDto.setImage(imageUrl);
        fakeStoreProductRequestDto.setCategory(category);

        FakeStoreProductDto fakeStoreProductDto = restTemplate.postForObject(fakeStoreUrl, fakeStoreProductRequestDto, FakeStoreProductDto.class);
        return fakeStoreProductDto.toProduct();
    }

    @Override
    public Product updateProduct(Long id, String name, String description, double price, String imageUrl, String category) throws ProductNotFoundException {
        FakeStoreProductRequestDto updateFakeStoreProductDto = new FakeStoreProductRequestDto();
        updateFakeStoreProductDto.setTitle(name);
        updateFakeStoreProductDto.setDescription(description);
        updateFakeStoreProductDto.setPrice(price);
        updateFakeStoreProductDto.setImage(imageUrl);
        updateFakeStoreProductDto.setCategory(category);

        restTemplate.put(fakeStoreUrl+id, updateFakeStoreProductDto);
        return getProductById(id);
    }

    @Override
    public String deleteProduct(Long id)
    {
        restTemplate.delete(fakeStoreUrl+id);
        return "Product in ID "+id+" Successfully Deleted";
    }
}
