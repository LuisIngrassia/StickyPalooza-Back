package com.g12.tpo.server.entity;

import java.math.BigDecimal;
import java.util.Set;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@AllArgsConstructor
@Data
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "bill_date", nullable = false)
    private Date billDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<BillProduct> billProducts;    
    
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = true)
    private PaymentMethod paymentMethod;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid;  

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return id != null && id.equals(bill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
