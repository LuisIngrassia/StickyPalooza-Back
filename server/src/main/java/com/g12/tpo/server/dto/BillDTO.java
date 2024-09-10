package com.g12.tpo.server.dto;

import java.math.BigDecimal;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BillDTO {
    private Long id;
    private Long userId;
    private Set<Long> productIds;
    private BigDecimal totalAmount;
}