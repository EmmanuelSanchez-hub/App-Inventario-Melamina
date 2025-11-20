package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para movimiento
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoResponse {
    
    private Long id;
    private String tipoMovimiento;
    private LocalDateTime fechaMovimiento;
    
    // Producto
    private Long productoId;
    private String codigoProducto;
    private String nombreProducto;
    
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal total;
    private String motivo;
    
    // Terceros
    private String proveedor;
    private String cliente;
    
    // Usuario que registró
    private String usuario;
    
    private String observaciones;
    private LocalDateTime createdAt;
}