package com.g12.tpo.server.dto;

import java.util.Set;

public class CartDTO {
    private Long id;
    private Long userId; // Assuming Cart is related to a User
    private Set<Long> productIds; // Assuming Cart holds a list of product IDs

    // Getters and Setters
}
