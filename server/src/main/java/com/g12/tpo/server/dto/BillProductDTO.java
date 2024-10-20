package com.g12.tpo.server.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillProductDTO {
    private Long id;
    private Long billId;
    private Long productId;
    private int quantity;
    private BigDecimal productPrice;
    private String productName;
}

