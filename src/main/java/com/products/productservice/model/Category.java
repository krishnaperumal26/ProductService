package com.products.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;

import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Category  extends BaseModel implements Serializable {
    private String description;

    @OneToMany(mappedBy = "category")
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private List<Product> products;
}
