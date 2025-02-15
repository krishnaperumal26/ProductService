package com.products.productservice.dtos;

import lombok.Data;

@Data
public class ErrorDto
{
    private String status;
    private String message;
}
