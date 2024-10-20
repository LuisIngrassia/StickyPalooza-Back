package com.g12.tpo.server.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private Long userId;
    private Date orderDate;
    private List<OrderProductDTO> orderProducts;
    private BigDecimal totalAmount;
    private boolean isConvertedToBill;
}
