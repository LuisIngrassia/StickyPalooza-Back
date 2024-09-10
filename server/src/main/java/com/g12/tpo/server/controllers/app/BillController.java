package com.g12.tpo.server.controllers.app;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.g12.tpo.server.dto.BillDTO;
import com.g12.tpo.server.dto.ProductDTO;
import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.entity.BillProduct;
import com.g12.tpo.server.entity.PaymentMethod;
import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.service.interfaces.BillService;
import com.g12.tpo.server.service.interfaces.OrderService;
import com.g12.tpo.server.repository.ProductRepository;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    private Bill convertToEntity(BillDTO dto, List<ProductDTO> productDTOs) {

        Order order = orderService.getOrderById(dto.getOrderId())
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + dto.getOrderId()));

        Bill bill = new Bill();
        bill.setOrder(order);
        bill.setTotalAmount(dto.getTotalAmount());
        bill.setPaymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()));

        Set<BillProduct> billProducts = productDTOs.stream()
                .map(productDTO -> {
                    Product product = productRepository.findById(productDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    BillProduct billProduct = new BillProduct();
                    billProduct.setBill(bill);
                    billProduct.setProduct(product);
                    billProduct.setQuantity(productDTO.getStockQuantity());
                    return billProduct;
                })
                .collect(Collectors.toCollection(HashSet::new)); 

        bill.setBillProducts(billProducts);

        return bill;
    }

    private BillDTO convertToDTO(Bill bill) {
        return BillDTO.builder()
            .id(bill.getId())
            .orderId(bill.getOrder().getId())
            .totalAmount(bill.getTotalAmount())
            .paymentMethod(bill.getPaymentMethod().name())
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

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO billDTO, @RequestBody List<ProductDTO> productDTOs) {
        Bill bill = convertToEntity(billDTO, productDTOs);
        Bill createdBill = billService.createBill(bill);
        return ResponseEntity.ok(convertToDTO(createdBill));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<BillDTO> updateBill(@PathVariable Long id, @RequestBody BillDTO billDTO, @RequestBody List<ProductDTO> productDTOs) {
        Bill bill = convertToEntity(billDTO, productDTOs);
        Bill updatedBill = billService.updateBill(id, bill);
        return ResponseEntity.ok(convertToDTO(updatedBill));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
