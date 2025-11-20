package com.cesarcort.inventario.dto.mapper;

import com.cesarcort.inventario.dto.response.MovimientoResponse;
import com.cesarcort.inventario.entidades.Movimiento;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir Movimiento a MovimientoResponse
 */
@Component
public class MovimientoMapper {
    
    public MovimientoResponse toResponse(Movimiento movimiento) {
        String proveedor = null;
        if (movimiento.getProveedor() != null) {
            proveedor = movimiento.getProveedor().getRazonSocial();
        }
        
        String cliente = null;
        if (movimiento.getCliente() != null) {
            if (movimiento.getCliente().getRazonSocial() != null) {
                cliente = movimiento.getCliente().getRazonSocial();
            } else {
                cliente = movimiento.getCliente().getNombres() + " " + movimiento.getCliente().getApellidos();
            }
        }
        
        return MovimientoResponse.builder()
            .id(movimiento.getId())
            .tipoMovimiento(movimiento.getTipoMovimiento().name())
            .fechaMovimiento(movimiento.getFechaMovimiento())
            .productoId(movimiento.getProducto().getId())
            .codigoProducto(movimiento.getProducto().getCodigo())
            .nombreProducto(movimiento.getProducto().getNombre())
            .cantidad(movimiento.getCantidad())
            .precioUnitario(movimiento.getPrecioUnitario())
            .total(movimiento.getTotal())
            .motivo(movimiento.getMotivo())
            .proveedor(proveedor)
            .cliente(cliente)
            .usuario(movimiento.getUsuario().getUsername())
            .observaciones(movimiento.getObservaciones())
            .createdAt(movimiento.getCreatedAt())
            .build();
    }
}