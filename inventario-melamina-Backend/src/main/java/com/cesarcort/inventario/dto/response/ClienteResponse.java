package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para cliente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponse {
    
    private Long id;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private String razonSocial;
    private String telefono;
    private String email;
    private String direccion;
    private String ciudad;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}