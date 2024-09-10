package com.g12.tpo.server.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDTO {

    private Long id;
    private Date orderDate;
    private BigDecimal totalAmount;
    private Long userId;
    private Long billId;
    private String paymentMethod; 
}
