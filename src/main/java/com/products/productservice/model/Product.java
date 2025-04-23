package com.products.productservice.model;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Product extends BaseModel implements Serializable {

    @Column(length = 10000)
    private String description;
    private String imageUrl;
    private double price;
    @ManyToOne
    private Category category;
}
