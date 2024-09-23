package com.g12.tpo.server.exceptions;

public class BillNotFoundException extends RuntimeException {
    public BillNotFoundException(Long id) {
        super("Bill not found with id: " + id);
    }
}

