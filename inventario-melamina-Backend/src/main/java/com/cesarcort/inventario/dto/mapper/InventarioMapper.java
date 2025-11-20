package com.cesarcort.inventario.dto.mapper;

import com.cesarcort.inventario.dto.response.InventarioResponse;
import com.cesarcort.inventario.entidades.Inventario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper para convertir Inventario a InventarioResponse
 */
@Component
public class InventarioMapper {
    
    public InventarioResponse toResponse(Inventario inventario) {
        BigDecimal valorUnitario = inventario.getProducto().getPrecioCompra();
        BigDecimal valorTotal = valorUnitario.multiply(BigDecimal.valueOf(inventario.getCantidadActual()));
        
        String estadoStock = calcularEstadoStock(
            inventario.getCantidadActual(), 
            inventario.getProducto().getStockMinimo()
        );
        
        return InventarioResponse.builder()
            .id(inventario.getId())
            .productoId(inventario.getProducto().getId())
            .codigoProducto(inventario.getProducto().getCodigo())
            .nombreProducto(inventario.getProducto().getNombre())
            .categoria(inventario.getProducto().getCategoria().getNombre())
            .cantidadActual(inventario.getCantidadActual())
            .stockMinimo(inventario.getProducto().getStockMinimo())
            .ubicacion(inventario.getUbicacion())
            .estadoStock(estadoStock)
            .valorUnitario(valorUnitario)
            .valorTotal(valorTotal)
            .ultimaActualizacion(inventario.getUltimaActualizacion())
            .build();
    }
    
    private String calcularEstadoStock(Integer stockActual, Integer stockMinimo) {
        if (stockActual == 0) {
            return "SIN_STOCK";
        } else if (stockActual <= stockMinimo) {
            return "CRÍTICO";
        } else if (stockActual <= (stockMinimo * 2)) {
            return "BAJO";
        } else {
            return "NORMAL";
        }
    }
}