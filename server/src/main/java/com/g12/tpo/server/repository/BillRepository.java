package com.g12.tpo.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.g12.tpo.server.entity.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    // Corrigiendo la referencia a la entidad Bill en lugar de la tabla bills
    @Query("select b from Bill b where b.user.id = ?1")
    List<Bill> findByUserId(Long userId);

    @Query("select b from Bill b where b.totalAmount > ?1")
    List<Bill> findByTotalAmountGreaterThan(Double amount);
}
