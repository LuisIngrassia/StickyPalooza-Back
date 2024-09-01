package com.g12.tpo.server.entity.dto;

import lombok.Data;
import java.util.Set;

@Data
public class CartDTO {
    private Long id;
    private Long userId; 
    private Set<Long> productIds; 
}
