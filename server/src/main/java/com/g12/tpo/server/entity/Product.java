package com.g12.tpo.server.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "original_price", nullable = false)
    private BigDecimal originalPrice;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "discount_percentage", nullable = false)
    private BigDecimal discountPercentage = BigDecimal.ZERO; // Default to 0%

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    public void applyDiscount(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage != null ? discountPercentage : BigDecimal.ZERO;

        if (this.discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            this.price = originalPrice.subtract(
                originalPrice.multiply(discountPercentage).divide(BigDecimal.valueOf(100))
            );
        } else {
            this.price = originalPrice;
        }
    }
}
