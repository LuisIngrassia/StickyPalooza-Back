package com.g12.tpo.server.entity.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String password; // Handle password securely in production
    private String firstName;
    private String lastName;
    private List<Long> orderIds; // Assuming User has a list of Order IDs
    private String role; // Enum values are often represented as strings in DTOs

    // Getters and Setters
}
