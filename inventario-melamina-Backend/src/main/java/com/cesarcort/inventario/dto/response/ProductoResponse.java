package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para producto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponse {
    
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String categoria;
    private Long categoriaId;
    
    // Características
    private String color;
    private String textura;
    private BigDecimal espesor;
    private BigDecimal largo;
    private BigDecimal ancho;
    private String unidadMedida;
    
    // Precios y stock
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private Integer stockMinimo;
    private Integer stockActual;
    
    // Proveedor
    private String proveedor;
    private Long proveedorId;
    
    private Boolean activo;
    private String estadoStock; // NORMAL, BAJO, CRÍTICO
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}