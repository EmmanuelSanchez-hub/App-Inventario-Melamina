package com.cesarcort.inventario.excepciones;

/**
 * Excepción lanzada cuando no hay suficiente stock para una operación
 */
public class StockInsuficienteException extends RuntimeException {
    
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
    
    public StockInsuficienteException(String producto, Integer stockActual, Integer cantidadSolicitada) {
        super(String.format("Stock insuficiente para el producto '%s'. Stock actual: %d, Cantidad solicitada: %d", 
            producto, stockActual, cantidadSolicitada));
    }
}
