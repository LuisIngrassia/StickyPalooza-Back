package com.g12.tpo.server.controllers.app;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.g12.tpo.server.dto.BillDTO;
import com.g12.tpo.server.dto.BillProductDTO;
import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.entity.BillProduct;
import com.g12.tpo.server.entity.PaymentMethod;
import com.g12.tpo.server.service.interfaces.BillService;

@RestController
@RequestMapping("/bills")
@CrossOrigin(origins = "http://localhost:5173")
public class BillController {

    @Autowired
    private BillService billService;

    // Convert Bill entity to BillDTO
    private BillDTO convertToDTO(Bill bill) {
        return BillDTO.builder()
                .orderId(bill.getOrder().getId())  
                .userId(bill.getUser().getId())
                .billDate(bill.getBillDate())
                .totalAmount(bill.getTotalAmount())
                .paymentMethod(bill.getPaymentMethod() != null ? bill.getPaymentMethod().name() : null)
                .isPaid(bill.isPaid())
                .cartProducts(
                        bill.getBillProducts().stream().map(this::convertToBillProductDTO).collect(Collectors.toList())
                )
                .build();
    }

    private BillProductDTO convertToBillProductDTO(BillProduct billProduct) {
        return BillProductDTO.builder()
                .id(billProduct.getId())
                .billId(billProduct.getBill().getId())
                .productId(billProduct.getProduct().getId())
                .quantity(billProduct.getQuantity())
                .productPrice(billProduct.getProduct().getPrice())
                .productName(billProduct.getProduct().getName())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        Bill bill = billService.getBillById(id);
        return ResponseEntity.ok(convertToDTO(bill));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<BillDTO>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        List<BillDTO> billDTOs = bills.stream()
                                       .map(this::convertToDTO)
                                       .collect(Collectors.toList());
        return ResponseEntity.ok(billDTOs);
    }

    @GetMapping("/billsFromUser/{userId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<BillDTO>> getBillsByUserId(@PathVariable Long userId) {
        List<Bill> bills = billService.getBillsByUserId(userId);
        List<BillDTO> billDTOs = bills.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(billDTOs);
    }

    @PostMapping("/convertOrderToBill/{orderId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<?> convertOrderToBill(@PathVariable Long orderId, @RequestParam String paymentMethod) {
        if (!billService.isValidPaymentMethod(paymentMethod)) {
            return ResponseEntity.badRequest().body("Invalid payment method");
        }
        
        PaymentMethod method = PaymentMethod.valueOf(paymentMethod.toUpperCase());
        Bill bill = billService.convertOrderToBill(orderId, method); 
        return ResponseEntity.ok(convertToDTO(bill));
    }
}