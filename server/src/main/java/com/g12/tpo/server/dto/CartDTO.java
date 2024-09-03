package com.g12.tpo.server.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Builder
@Data
public class CartDTO {
    private Long id;
    private Long userId; 
    private Set<Long> productIds; 
}
