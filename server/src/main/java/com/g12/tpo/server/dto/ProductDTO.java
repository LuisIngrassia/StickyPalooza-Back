package com.g12.tpo.server.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String image; 
    private int stockQuantity;
    private Long categoryId; 
}
