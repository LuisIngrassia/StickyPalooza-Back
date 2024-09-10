package com.g12.tpo.server.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CartDTO {
    private Long id;
    private Long userId;
    private Map<Long, Integer> productQuantities;
}
