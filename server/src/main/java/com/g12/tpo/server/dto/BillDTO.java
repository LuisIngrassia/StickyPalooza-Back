package com.g12.tpo.server.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class BillDTO {
    private Long id;
    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private String paymentMethod;
}
