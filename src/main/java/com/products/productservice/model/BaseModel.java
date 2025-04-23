package com.products.productservice.model;

import jakarta.annotation.security.DenyAll;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseModel implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Date createdAt;
    private Date lastModified;
    private boolean isDeleted;
}
