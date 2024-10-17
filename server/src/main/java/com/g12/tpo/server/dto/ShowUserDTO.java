package com.g12.tpo.server.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class ShowUserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
