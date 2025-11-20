package com.cesarcort.inventario.dto.mapper;

import com.cesarcort.inventario.dto.response.ProveedorResponse;
import com.cesarcort.inventario.entidades.Proveedor;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir Proveedor a ProveedorResponse
 */
@Component
public class ProveedorMapper {
    
    public ProveedorResponse toResponse(Proveedor proveedor) {
        return ProveedorResponse.builder()
            .id(proveedor.getId())
            .razonSocial(proveedor.getRazonSocial())
            .ruc(proveedor.getRuc())
            .contacto(proveedor.getContacto())
            .telefono(proveedor.getTelefono())
            .email(proveedor.getEmail())
            .direccion(proveedor.getDireccion())
            .ciudad(proveedor.getCiudad())
            .activo(proveedor.getActivo())
            .createdAt(proveedor.getCreatedAt())
            .updatedAt(proveedor.getUpdatedAt())
            .build();
    }
}