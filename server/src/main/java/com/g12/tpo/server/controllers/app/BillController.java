package com.g12.tpo.server.controllers.app;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.entity.dto.BillDTO;
import com.g12.tpo.server.service.interfaces.BillService;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillService billService;

    private Bill convertToEntity(BillDTO dto) {
        Bill bill = new Bill();
        bill.setId(dto.getId());

        User user = new User();
        user.setId(dto.getUserId());
        bill.setUser(user);

        Set<Product> products = dto.getProductIds().stream()
                                   .map(productId -> {
                                       Product product = new Product();
                                       product.setId(productId);
                                       return product;
                                   })
                                   .collect(Collectors.toSet());
        bill.setProducts(products);
        bill.setTotalAmount(dto.getTotalAmount());

        return bill;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO billDTO) {
        Bill bill = convertToEntity(billDTO);
        Bill createdBill = billService.createBill(bill);
        return ResponseEntity.ok(convertToDTO(createdBill));
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<BillDTO> updateBill(@PathVariable Long id, @RequestBody BillDTO billDTO) {
        Bill bill = convertToEntity(billDTO);
        Bill updatedBill = billService.updateBill(id, bill);
        return ResponseEntity.ok(convertToDTO(updatedBill));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }

    private BillDTO convertToDTO(Bill bill) {
        BillDTO dto = new BillDTO();
        dto.setId(bill.getId());
        dto.setUserId(bill.getUser().getId());
        dto.setProductIds(bill.getProducts().stream()
                                .map(product -> product.getId())
                                .collect(Collectors.toSet()));
        dto.setTotalAmount(bill.getTotalAmount());
        return dto;
    }

}