package com.g12.tpo.server.service.interfaces;

import java.util.List;
import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.entity.PaymentMethod;

public interface BillService {
    Bill convertOrderToBill(Long orderId, PaymentMethod paymentMethod);
    Bill getBillById(Long id);
    List<Bill> getAllBills();
    List<Bill> getBillsByUserId(Long userId);
    boolean isValidPaymentMethod(String paymentMethod); 
    public Bill markBillAsPaid(Long orderId);
}
