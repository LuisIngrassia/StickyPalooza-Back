package com.g12.tpo.server.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ContactDTO {
    private Long id;
    private String fullName;
    private String problemType;
    private String description;
}
