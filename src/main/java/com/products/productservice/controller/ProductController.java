package com.products.productservice.controller;

import com.products.productservice.dtos.CreateProductRequestDto;
import com.products.productservice.dtos.FakeStoreProductDto;
import com.products.productservice.dtos.FakeStoreProductRequestDto;
import com.products.productservice.dtos.ProductResponseDto;
import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;
import com.products.productservice.services.ProductService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    private RedisTemplate<String, Object> redisTemplate;

    ProductController(ProductService productService, RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(
            @PathVariable("id") long id ) throws ProductNotFoundException {

        Product productFromCache = (Product)redisTemplate.opsForHash().get("PRODUCTMAP","PRODUCT_"+id);
//        Product product = redisProduct;
//        if(redisProduct == null) {
//            product = productService.getProductById(id);
//            redisTemplate.opsForHash().put("PRODUCTMAP","PRODUCT_"+id,product);
//        }


        Product product = productService.getProductById(id);
        ProductResponseDto productResponseDto =  ProductResponseDto.fromEntity(product);

        ResponseEntity<ProductResponseDto> responseEntity =
                new ResponseEntity<>(productResponseDto, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<Product> productList = productService.getAllProducts();
        List<ProductResponseDto> productResponseDtos =  new ArrayList<ProductResponseDto>();

        for(Product product : productList) {
            ProductResponseDto productResponseDto = ProductResponseDto.fromEntity(product);
            productResponseDtos.add(productResponseDto);
        }
        ResponseEntity<List<ProductResponseDto>> responseEntity = new ResponseEntity<>(productResponseDtos, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping()
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody CreateProductRequestDto createProductRequestDto) {
        Product product = productService.createProduct(
                createProductRequestDto.getName(),
                createProductRequestDto.getDescription(),
                createProductRequestDto.getPrice(),
                createProductRequestDto.getImageUrl(),
                createProductRequestDto.getCategory());

        ProductResponseDto productResponseDto =  ProductResponseDto.fromEntity(product);
        return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable long id,
            @RequestBody CreateProductRequestDto createProductRequestDto) throws ProductNotFoundException {
        Product product = productService.updateProduct(id,
                createProductRequestDto.getName(),
                createProductRequestDto.getDescription(),
                createProductRequestDto.getPrice(),
                createProductRequestDto.getImageUrl(),
                createProductRequestDto.getCategory());
        ProductResponseDto productResponseDto =  ProductResponseDto.fromEntity(product);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        String response = productService.deleteProduct(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


//    @ExceptionHandler(NullPointerException.class)
//    public ErrorDto handleNullPointerException()
//    {
//        ErrorDto errorDto = new ErrorDto();
//        errorDto.setMessage("Null pointer exception");
//        errorDto.setStatus("Failure");
//        return errorDto;
//    }
}
