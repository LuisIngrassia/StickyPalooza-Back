package com.g12.tpo.server.controllers.auth;

import com.g12.tpo.server.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;

    // Lo cambie por una funcion de registro especifica para ADMIN y otra para USER, se puede bindear la creacion de ADMIN unicamente a ciertos IPS pero 
    // preferible preguntarle a la profe antes.
    private Role role;
}
