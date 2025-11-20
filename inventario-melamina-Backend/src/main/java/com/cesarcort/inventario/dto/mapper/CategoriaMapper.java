package com.cesarcort.inventario.dto.mapper;

import com.cesarcort.inventario.dto.response.CategoriaResponse;
import com.cesarcort.inventario.entidades.Categoria;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir Categoria a CategoriaResponse
 */
@Component
public class CategoriaMapper {
    
    public CategoriaResponse toResponse(Categoria categoria) {
        // Verificar si la colección de productos está inicializada
        Integer cantidadProductos = 0;
        try {
            if (Hibernate.isInitialized(categoria.getProductos())) {
                cantidadProductos = categoria.getProductos() != null ? categoria.getProductos().size() : 0;
            }
        } catch (Exception e) {
            // Si hay error al acceder, dejamos en 0
            cantidadProductos = 0;
        }
        
        return CategoriaResponse.builder()
            .id(categoria.getId())
            .nombre(categoria.getNombre())
            .descripcion(categoria.getDescripcion())
            .activo(categoria.getActivo())
            .cantidadProductos(cantidadProductos)
            .createdAt(categoria.getCreatedAt())
            .updatedAt(categoria.getUpdatedAt())
            .build();
    }
}