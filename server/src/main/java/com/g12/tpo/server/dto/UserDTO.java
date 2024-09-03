package com.g12.tpo.server.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String password;
    private String firstName;
    private String lastName;
    private Long cartId;
    private List<Long> orderIds;
    private List<Long> billIds;
    private String role;
}
