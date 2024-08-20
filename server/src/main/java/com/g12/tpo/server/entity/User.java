package com.g12.tpo.server.entity;

import java.util.List;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;
    
    @Column
    private String name;

    @Column
    private String username;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
