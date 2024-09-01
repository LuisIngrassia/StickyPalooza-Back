package com.g12.tpo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.entity.Bill;

@Service
public interface BillRepository extends JpaRepository<Bill, Long> {
    
}

