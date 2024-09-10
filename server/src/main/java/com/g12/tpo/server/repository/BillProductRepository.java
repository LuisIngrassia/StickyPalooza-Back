package com.g12.tpo.server.repository;

import com.g12.tpo.server.entity.BillProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillProductRepository extends JpaRepository<BillProduct, Long> {
}
