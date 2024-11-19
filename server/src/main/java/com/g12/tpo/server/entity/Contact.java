package com.g12.tpo.server.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name" , nullable = false)
    private String fullName;

    @Column(name = "problem_type" , nullable = false)
    private String problemType;

    @Lob
    @Column(name = "description")
    private String description;

    @ElementCollection
    @Column(name = "photo_urls")
    private List<String> photoUrls;

    @Column(name = "created_at" , nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
