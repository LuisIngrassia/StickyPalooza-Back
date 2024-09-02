package com.g12.tpo.server.service.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.repository.BillRepository;
import com.g12.tpo.server.service.interfaces.BillService;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Override
    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public Bill updateBill(Long id, Bill billDetails) {
        Bill bill = getBillById(id);
        bill.setProducts(billDetails.getProducts());
        bill.setTotalAmount(billDetails.getTotalAmount());
        bill.setUser(billDetails.getUser());
        return billRepository.save(bill);
    }

    @Override
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}
