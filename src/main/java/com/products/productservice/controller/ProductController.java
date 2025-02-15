package com.products.productservice.controller;

import com.products.productservice.dtos.CreateFakeStoreProductDto;
import com.products.productservice.dtos.ErrorDto;
import com.products.productservice.dtos.ProductResponseDto;
import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;
import com.products.productservice.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping("products/{id}")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProducts(@PathVariable long id ) throws ProductNotFoundException {

        Product product = productService.getProductById(id);
        ProductResponseDto productResponseDto =  new ProductResponseDto().fromEntity(product);

        ResponseEntity<ProductResponseDto> responseEntity =
                new ResponseEntity<>(productResponseDto, HttpStatus.OK);

        return responseEntity;
    }

//    @GetMapping("/products")
    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<Product> productList = productService.getAllProducts();
        List<ProductResponseDto> productResponseDtos =  new ArrayList<ProductResponseDto>();
        for(Product product : productList) {
            productResponseDtos.add(new ProductResponseDto().fromEntity(product));
        }

        ResponseEntity<List<ProductResponseDto>> responseEntity = new ResponseEntity<>(productResponseDtos, HttpStatus.OK);
        return responseEntity;
    }

//    @PostMapping("/products")
    @PostMapping()
    public ProductResponseDto createProduct(@RequestBody CreateFakeStoreProductDto createFakeStoreProductDto) {
        Product product = productService.createProduct(createFakeStoreProductDto.getTitle(),
                createFakeStoreProductDto.getPrice(), createFakeStoreProductDto.getDescription(),
                createFakeStoreProductDto.getCategory(), createFakeStoreProductDto.getImage());

        ProductResponseDto productResponseDto =  new ProductResponseDto().fromEntity(product);

        return productResponseDto;
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
