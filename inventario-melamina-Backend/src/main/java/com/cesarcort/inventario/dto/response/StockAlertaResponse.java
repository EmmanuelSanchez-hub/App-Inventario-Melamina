package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para alertas de stock
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAlertaResponse {
    
    private Long productoId;
    private String codigo;
    private String nombre;
    private Integer stockActual;
    private Integer stockMinimo;
    private String nivelAlerta; // CRÍTICO, BAJO
    private String categoria;
}