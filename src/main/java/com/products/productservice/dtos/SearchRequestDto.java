package com.products.productservice.dtos;

import lombok.Data;

@Data
public class SearchRequestDto {
    private String query;
    private int pageNumber;
    private int pageSize;
    private String sortParam;
}
