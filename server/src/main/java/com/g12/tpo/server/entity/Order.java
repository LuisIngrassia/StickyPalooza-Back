package com.g12.tpo.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date orderDate;
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<Product> products;
}
