package com.kp.firstspringproject.controllers;

import com.kp.firstspringproject.models.Product;
import com.kp.firstspringproject.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    ProductController(ProductService productService)
    {
        this.productService = productService;
    }



//localhost:2020/products/1
  /*  @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") Long id)
    {
        return productService.getProductById(id);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id)
    {
        Product product =  productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

//localhost:8080/products
    @GetMapping()
    public List<Product> getAllProduct()
    {
        return productService.getAllProduct();
    }

    @PutMapping("/{id}")
    public Product replaceProduct(@PathVariable("id") Long id, Product product)
    {
        return productService.replaceProduct(id,product);
    }

    //Create Product
    //Update Product - Patch
    //delete Product
    //Replace product
}
