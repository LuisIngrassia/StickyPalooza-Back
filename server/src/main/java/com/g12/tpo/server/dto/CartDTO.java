package com.g12.tpo.server.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartProductDTO> cartProducts;
}
