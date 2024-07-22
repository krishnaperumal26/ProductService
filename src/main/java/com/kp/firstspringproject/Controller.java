package com.kp.firstspringproject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//This Controller will be containing HTTP API's

@RestController
//localhost:8080/products - All API's for /products should come to this controller
@RequestMapping("/products")
public class Controller {
    //localhost:8080/products/myProducts
    @GetMapping("/myProducts/{name}")
    public String MyProducts(@PathVariable("name") String name)
    {
        return "This is my product which is "+name;
    }
    //localhost:8080/products/publicProducts
    @GetMapping("/publicProduct")
    public String publicProducts()
    {
        return "This is general Product";
    }
}
