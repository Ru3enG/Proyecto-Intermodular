package com.proyecto.proyecto.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String mensaje){
        super(mensaje);
    }
}
