package com.g12.tpo.server.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
@Builder
public class BillDTO {
    private Long id;
    private Long userId;
    private Date billDate;
    private Map<Long, Integer> productQuantities;  
    private BigDecimal totalAmount;
    private String paymentMethod;
}
