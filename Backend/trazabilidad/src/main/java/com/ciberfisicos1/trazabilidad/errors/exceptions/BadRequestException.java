package com.ciberfisicos1.trazabilidad.errors.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
