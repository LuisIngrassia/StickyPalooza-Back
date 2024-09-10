package com.g12.tpo.server.service.implementations;

import java.util.List;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.entity.BillProduct;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.PaymentMethod;
import com.g12.tpo.server.repository.BillRepository;
import com.g12.tpo.server.service.interfaces.BillService;
import com.g12.tpo.server.service.interfaces.OrderService;

import jakarta.transaction.Transactional;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private OrderService orderService;

    @Transactional
    @Override
    public Bill convertOrderToBill(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        Bill bill = new Bill();
        bill.setOrder(order);
        bill.setBillDate(new Date()); 
        bill.setTotalAmount(order.getTotalAmount());
        bill.setUser(order.getUser());

        Set<BillProduct> billProducts = order.getOrderProducts().stream()
                .map(orderProduct -> {
                    BillProduct billProduct = new BillProduct();
                    billProduct.setBill(bill);
                    billProduct.setProduct(orderProduct.getProduct());
                    billProduct.setQuantity(orderProduct.getQuantity());
                    return billProduct;
                })
                .collect(Collectors.toSet());

        bill.setBillProducts(billProducts);
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
        bill.setBillProducts(billDetails.getBillProducts());
        bill.setTotalAmount(billDetails.getTotalAmount());
        bill.setUser(billDetails.getUser());
        return billRepository.save(bill);
    }

    @Override
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}
