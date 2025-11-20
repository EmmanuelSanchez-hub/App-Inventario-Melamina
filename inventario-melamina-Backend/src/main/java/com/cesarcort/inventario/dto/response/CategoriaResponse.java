package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para categoría
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaResponse {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private Integer cantidadProductos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}