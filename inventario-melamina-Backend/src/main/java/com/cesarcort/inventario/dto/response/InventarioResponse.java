package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para inventario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponse {
    
    private Long id;
    private Long productoId;
    private String codigoProducto;
    private String nombreProducto;
    private String categoria;
    private Integer cantidadActual;
    private Integer stockMinimo;
    private String ubicacion;
    private String estadoStock; // NORMAL, BAJO, CRÍTICO, SIN_STOCK
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private LocalDateTime ultimaActualizacion;
}