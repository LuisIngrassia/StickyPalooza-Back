package com.g12.tpo.server.entity.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId; // Assuming Product is related to a Category
    private int stockQuantity;

    // Getters and Setters
}
