package com.g12.tpo.server.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {
    private int id;
    private String description;
}
