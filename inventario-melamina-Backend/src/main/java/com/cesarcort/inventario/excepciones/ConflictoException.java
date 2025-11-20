package com.cesarcort.inventario.excepciones;

/**
 * Excepción lanzada cuando hay un conflicto (ej: registro duplicado)
 * Se traduce en un HTTP 409 Conflict
 */
public class ConflictoException extends RuntimeException {
    
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}