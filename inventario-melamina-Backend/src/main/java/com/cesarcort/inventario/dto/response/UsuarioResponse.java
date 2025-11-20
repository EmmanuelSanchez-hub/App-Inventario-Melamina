package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    
    private Long id;
    private String username;
    private String email;
    private String nombres;
    private String apellidos;
    private String telefono;
    private Boolean activo;
    private String rol;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}