package com.g12.tpo.server.service.interfaces;

import java.util.List;

import com.g12.tpo.server.entity.Bill;

public interface BillService {
    Bill createBill(Bill bill);
    Bill getBillById(Long id);
    List<Bill> getAllBills();
    Bill updateBill(Long id, Bill billDetails);
    void deleteBill(Long id);
}
