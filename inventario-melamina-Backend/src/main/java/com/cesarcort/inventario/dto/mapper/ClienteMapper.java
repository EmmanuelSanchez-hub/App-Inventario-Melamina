package com.cesarcort.inventario.dto.mapper;

import com.cesarcort.inventario.dto.response.ClienteResponse;
import com.cesarcort.inventario.entidades.Cliente;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir Cliente a ClienteResponse
 */
@Component
public class ClienteMapper {
    
    public ClienteResponse toResponse(Cliente cliente) {
        return ClienteResponse.builder()
            .id(cliente.getId())
            .tipoDocumento(cliente.getTipoDocumento())
            .numeroDocumento(cliente.getNumeroDocumento())
            .nombres(cliente.getNombres())
            .apellidos(cliente.getApellidos())
            .razonSocial(cliente.getRazonSocial())
            .telefono(cliente.getTelefono())
            .email(cliente.getEmail())
            .direccion(cliente.getDireccion())
            .ciudad(cliente.getCiudad())
            .activo(cliente.getActivo())
            .createdAt(cliente.getCreatedAt())
            .updatedAt(cliente.getUpdatedAt())
            .build();
    }
}