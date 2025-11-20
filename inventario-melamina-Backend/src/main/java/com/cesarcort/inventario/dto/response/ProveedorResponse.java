package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para proveedor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorResponse {
    
    private Long id;
    private String razonSocial;
    private String ruc;
    private String contacto;
    private String telefono;
    private String email;
    private String direccion;
    private String ciudad;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}