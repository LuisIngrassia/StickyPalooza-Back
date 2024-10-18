package com.g12.tpo.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ShowUserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password; 
}
