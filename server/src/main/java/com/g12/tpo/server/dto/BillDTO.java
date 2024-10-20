package com.g12.tpo.server.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class BillDTO {
    private Long orderId;
    private Long userId;
    private Date billDate;
    private List<BillProductDTO> cartProducts;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private boolean isPaid;
}
