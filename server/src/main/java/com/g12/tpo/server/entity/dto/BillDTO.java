package com.g12.tpo.server.entity.dto;

import java.util.Set;

import lombok.Data;

@Data
public class BillDTO {
    private Long id;
    private Long userId;
    private Set<Long> productIds;
    private Double totalAmount;
}