package com.g12.tpo.server.dto;

import lombok.Data;
import java.util.Set;

@Data
public class CartDTO {
    private Long id;
    private Long userId; // ID del usuario relacionado
    private Set<Long> productIds; // IDs de los productos en el carrito

    // Getters y Setters generados automáticamente por Lombok
}
