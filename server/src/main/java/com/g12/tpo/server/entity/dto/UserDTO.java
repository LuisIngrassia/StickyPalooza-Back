package com.g12.tpo.server.entity.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String password;
    private String firstName;
    private String lastName;
    private List<Long> orderIds;
    private String role;
}
