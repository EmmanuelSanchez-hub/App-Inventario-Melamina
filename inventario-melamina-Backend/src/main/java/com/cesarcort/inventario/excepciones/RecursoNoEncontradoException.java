package com.cesarcort.inventario.excepciones;

/**
 * Excepción lanzada cuando no se encuentra un recurso solicitado
 * Se traduce en un HTTP 404 Not Found
 */
public class RecursoNoEncontradoException extends RuntimeException {
    
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    
    public RecursoNoEncontradoException(String recurso, String campo, Object valor) {
        super(String.format("%s no encontrado con %s: '%s'", recurso, campo, valor));
    }
}