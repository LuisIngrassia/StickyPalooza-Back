package com.g12.tpo.server.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryDTO {
    private Long id;
    private String description;
}
