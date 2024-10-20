package com.g12.tpo.server.service.implementations;

import java.util.List;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.entity.BillProduct;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.PaymentMethod;
import com.g12.tpo.server.repository.BillRepository;
import com.g12.tpo.server.service.interfaces.BillService;
import com.g12.tpo.server.service.interfaces.OrderService;
import com.g12.tpo.server.exceptions.BillNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private OrderService orderService;

    @Transactional
    @Override
    public Bill convertOrderToBill(Long orderId, PaymentMethod paymentMethod) {
        try {
            // Retrieve the order by ID
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                logger.error("Order with ID {} not found", orderId);
                throw new IllegalArgumentException("Order not found");
            }

            // Set order as converted to bill
            order.setConvertedToBill(true);

            // Create a new bill
            Bill bill = new Bill();
            bill.setOrder(order);
            bill.setBillDate(new Date());
            bill.setTotalAmount(order.getTotalAmount());
            bill.setUser(order.getUser());
            bill.setPaymentMethod(paymentMethod);
            bill.setPaid(false);  // Set initial state as unpaid
            
            // Create BillProduct entities from the order's products
            Set<BillProduct> billProducts = order.getOrderProducts() != null ? 
                order.getOrderProducts().stream()
                    .map(orderProduct -> {
                        BillProduct billProduct = new BillProduct();
                        billProduct.setBill(bill);
                        billProduct.setProduct(orderProduct.getProduct());
                        billProduct.setQuantity(orderProduct.getQuantity());
                        return billProduct;
                    })
                    .collect(Collectors.toSet()) : new HashSet<>();
            
            bill.setBillProducts(billProducts);
            return billRepository.save(bill);
        } catch (Exception e) {
            logger.error("Error converting order to bill: {}", e.getMessage());
            throw e; // Rethrow the exception to handle it upstream
        }
    }

    @Transactional
    @Override
    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException(id));
    }
    
    @Transactional
    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
    
    @Override
    public List<Bill> getBillsByUserId(Long userId) {
        return billRepository.findByUserId(userId);
    }

    public boolean isValidPaymentMethod(String paymentMethod) {
        try {
            PaymentMethod.valueOf(paymentMethod.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Bill markBillAsPaid(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found for id: " + id));
        bill.setPaid(true);
        return billRepository.save(bill);
    }
}
