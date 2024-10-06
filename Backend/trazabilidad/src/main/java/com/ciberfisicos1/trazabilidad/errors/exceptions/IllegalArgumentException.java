package com.ciberfisicos1.trazabilidad.errors.exceptions;

import java.text.ParseException;

public class IllegalArgumentException extends RuntimeException {
    public IllegalArgumentException(String message) {
        super(message);
        }
    
        public IllegalArgumentException(String message, ParseException cause) {
            super(message, cause);
    }
}
