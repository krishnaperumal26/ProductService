package com.products.productservice.controller;

import com.products.productservice.dtos.ProductResponseDto;
import com.products.productservice.dtos.SearchRequestDto;
import com.products.productservice.model.Product;
import com.products.productservice.services.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchController {
    SearchService searchService;
    public SearchController(SearchService searchService)
    {
        this.searchService = searchService;
    }
    @PostMapping("/search")
    public Page<ProductResponseDto> search(@RequestBody SearchRequestDto searchRequestDto)
    {
        Page<Product> productPage =  searchService.search(searchRequestDto.getQuery(),
                searchRequestDto.getPageNumber(),
                searchRequestDto.getPageSize(),
                 searchRequestDto.getSortParam());

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        List<Product> products = productPage.getContent();
        for(Product product : products)
        {
            ProductResponseDto productResponseDto = ProductResponseDto.fromEntity(product);
            productResponseDtos.add(productResponseDto);
        }

        return new PageImpl<>(productResponseDtos, productPage.getPageable(), productPage.getTotalElements());
//        return new PageImpl<>(productResponseDtos);
    }
    @GetMapping("/search")
    public List<ProductResponseDto> search(@RequestParam String query,
                                @RequestParam int pageNumber,
                                @RequestParam int pageSize,
                                @RequestParam String sortParam) {
        Page<Product> productPage =  searchService.search(query, pageNumber, pageSize, sortParam);

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        List<Product> products = productPage.getContent();
        for(Product product : products)
        {
            ProductResponseDto productResponseDto = ProductResponseDto.fromEntity(product);
            productResponseDtos.add(productResponseDto);
        }
        return productResponseDtos;

    }
}
