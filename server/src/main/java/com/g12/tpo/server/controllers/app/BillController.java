package com.g12.tpo.server.controllers.app;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.g12.tpo.server.dto.BillDTO;
import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.exceptions.ProductNotFoundException;
import com.g12.tpo.server.service.interfaces.BillService;
import com.g12.tpo.server.service.interfaces.ProductService;
import com.g12.tpo.server.service.interfaces.UserService;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService; // Asegúrate de inyectar UserService

    private Bill convertToEntity(BillDTO dto) {
        // Cargar usuario desde UserService
        User user = userService.getUserById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        Bill bill = new Bill();
        bill.setUser(user);
        return bill;
    }

    private BillDTO convertToDTO(Bill bill) {
        return BillDTO.builder()
            .id(bill.getId())
            .userId(bill.getUser().getId())
            .productIds(bill.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toSet()))
            .totalAmount(bill.getTotalAmount())
            .build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO billDTO) {

        Bill bill = convertToEntity(billDTO);

        // Obtener productos desde el ProductService
        Set<Product> products = billDTO.getProductIds().stream()
            .map(productId -> {
                try {
                    return productService.getProductById(productId);
                } catch (ProductNotFoundException e) {
                    throw new RuntimeException("Product not found with ID: " + productId, e);
                }
            })
            .collect(Collectors.toSet());
        
        bill.setProducts(products);

        // Calcular totalAmount
        BigDecimal totalAmount = products.stream()
            .map(Product::getPrice)
            .filter(Objects::nonNull) // Filtrar nulls para evitar NullPointerException
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        bill.setTotalAmount(totalAmount);

        // Crear la factura
        Bill createdBill = billService.createBill(bill);

        // Convertir la entidad de vuelta a DTO
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
}
