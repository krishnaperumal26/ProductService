package com.kp.firstspringproject.controllers;

import com.kp.firstspringproject.models.Product;
import com.kp.firstspringproject.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") Long id)
    {
        return productService.getProductById(id);
    }
//localhost:8080/products
    @GetMapping()
    public List<Product> getAllProduct()
    {
        return productService.getAllProduct();
    }


    //Create Product
    //Update Product - Patch
    //delete Product
    //Replace product
}
