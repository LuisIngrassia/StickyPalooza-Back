package com.g12.tpo.server.dto;

import java.util.Date;
import java.util.Set;

public class OrderDTO {
    private Long id;
    private Date orderDate;
    private Double totalPrice;
    private Long userId; // Assuming Order is related to a User
    private Set<Long> productIds; // Assuming Order holds a list of product IDs

    // Getters and Setters
}
