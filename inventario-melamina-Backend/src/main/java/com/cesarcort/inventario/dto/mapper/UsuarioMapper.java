package com.cesarcort.inventario.dto.mapper;

import com.cesarcort.inventario.dto.response.UsuarioResponse;
import com.cesarcort.inventario.entidades.Usuario;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir Usuario a UsuarioResponse
 */
@Component
public class UsuarioMapper {
    
    public UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
            .id(usuario.getId())
            .username(usuario.getUsername())
            .email(usuario.getEmail())
            .nombres(usuario.getNombres())
            .apellidos(usuario.getApellidos())
            .telefono(usuario.getTelefono())
            .activo(usuario.getActivo())
            .rol(usuario.getRol().getNombre())
            .createdAt(usuario.getCreatedAt())
            .updatedAt(usuario.getUpdatedAt())
            .build();
    }
}