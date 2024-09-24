package com.g12.tpo.server.controllers.app;

import java.util.List;
//import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.g12.tpo.server.dto.BillDTO;
import com.g12.tpo.server.entity.Bill;
import com.g12.tpo.server.entity.BillProduct;
//import com.g12.tpo.server.entity.Order;
import com.g12.tpo.server.entity.PaymentMethod;
//import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.service.interfaces.BillService;
//mport com.g12.tpo.server.service.interfaces.OrderService;
//import com.g12.tpo.server.repository.ProductRepository;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillService billService;

 //   @Autowired
 //   private OrderService orderService;

 //   @Autowired
 //   private ProductRepository productRepository;

    // // Convert BillDTO to Bill entity with associated products
    // private Bill convertToEntity(BillDTO dto) {
    //     // Get the order using the order ID
    //     Order order = orderService.getOrderById(dto.getOrderId());
    
    //     Bill bill = new Bill();
    //     bill.setOrder(order);
    //     bill.setBillDate(dto.getBillDate());
    //     bill.setTotalAmount(dto.getTotalAmount());
        
    //     // Convert string payment method to PaymentMethod enum
    //     bill.setPaymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()));  // Add this line
    
    //     // Create BillProducts from the productQuantities in BillDTO
    //     Set<BillProduct> billProducts = dto.getProductQuantities().entrySet().stream()
    //             .map(entry -> {
    //                 Long productId = entry.getKey();
    //                 int quantity = entry.getValue();
    
    //                 Product product = productRepository.findById(productId)
    //                         .orElseThrow(() -> new RuntimeException("Product not found"));
    
    //                 BillProduct billProduct = new BillProduct();
    //                 billProduct.setBill(bill);
    //                 billProduct.setProduct(product);
    //                 billProduct.setQuantity(quantity);
    
    //                 return billProduct;
    //             })
    //             .collect(Collectors.toSet());
    
    //     bill.setBillProducts(billProducts);
    
    //     return bill;
    // }
    
    // Convert Bill entity to BillDTO
    private BillDTO convertToDTO(Bill bill) {
        return BillDTO.builder()
                .orderId(bill.getId())
                .userId(bill.getUser().getId())
                .billDate(bill.getBillDate())
                .totalAmount(bill.getTotalAmount())
                .productQuantities(
                        bill.getBillProducts().stream().collect(
                                Collectors.toMap(
                                        bp -> bp.getProduct().getId(),
                                        BillProduct::getQuantity
                                )
                        )
                )
                .build();
    }

    // GET method to fetch a Bill by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        Bill bill = billService.getBillById(id);
        return ResponseEntity.ok(convertToDTO(bill));
    }

    // GET all bills (ADMIN only)
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<BillDTO>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        List<BillDTO> billDTOs = bills.stream()
                                       .map(this::convertToDTO)
                                       .collect(Collectors.toList());
        return ResponseEntity.ok(billDTOs);
    }

    // POST method to convert an order to a bill
    @PostMapping("/convertOrderToBill/{orderId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<?> convertOrderToBill(@PathVariable Long orderId, @RequestParam String paymentMethod) {
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(paymentMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
        
        Bill bill = billService.convertOrderToBill(orderId, method); 
        return ResponseEntity.ok(convertToDTO(bill));
    }
}
