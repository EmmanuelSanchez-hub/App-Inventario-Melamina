package com.cesarcort.inventario.dto.mapper;

import com.cesarcort.inventario.dto.response.ProductoResponse;
import com.cesarcort.inventario.entidades.Producto;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir Producto a ProductoResponse
 */
@Component
public class ProductoMapper {
    
    public ProductoResponse toResponse(Producto producto) {
        Integer stockActual = 0;
        if (producto.getInventario() != null) {
            stockActual = producto.getInventario().getCantidadActual();
        }
        
        String estadoStock = calcularEstadoStock(stockActual, producto.getStockMinimo());
        
        return ProductoResponse.builder()
            .id(producto.getId())
            .codigo(producto.getCodigo())
            .nombre(producto.getNombre())
            .descripcion(producto.getDescripcion())
            .categoria(producto.getCategoria() != null ? producto.getCategoria().getNombre() : null)
            .categoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null)
            .color(producto.getColor())
            .textura(producto.getTextura())
            .espesor(producto.getEspesor())
            .largo(producto.getLargo())
            .ancho(producto.getAncho())
            .unidadMedida(producto.getUnidadMedida())
            .precioCompra(producto.getPrecioCompra())
            .precioVenta(producto.getPrecioVenta())
            .stockMinimo(producto.getStockMinimo())
            .stockActual(stockActual)
            .proveedor(producto.getProveedor() != null ? producto.getProveedor().getRazonSocial() : null)
            .proveedorId(producto.getProveedor() != null ? producto.getProveedor().getId() : null)
            .activo(producto.getActivo())
            .estadoStock(estadoStock)
            .createdAt(producto.getCreatedAt())
            .updatedAt(producto.getUpdatedAt())
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