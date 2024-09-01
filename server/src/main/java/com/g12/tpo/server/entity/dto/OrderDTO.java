package com.g12.tpo.server.entity.dto;

import java.util.Date;
import java.util.Set;
import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private Date orderDate;
    private Double totalPrice;
    private Long userId;
    private Set<Long> productIds;
}
