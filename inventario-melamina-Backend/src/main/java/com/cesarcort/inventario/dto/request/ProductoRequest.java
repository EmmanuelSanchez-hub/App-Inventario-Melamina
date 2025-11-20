package com.cesarcort.inventario.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para crear/actualizar productos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequest {
    
    @NotBlank(message = "El código es obligatorio")
    private String codigo;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
    
    // Características específicas de melamina/madera
    private String color;
    private String textura;
    private BigDecimal espesor;
    private BigDecimal largo;
    private BigDecimal ancho;
    private String unidadMedida = "UNIDAD";
    
    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de compra debe ser mayor a 0")
    private BigDecimal precioCompra;
    
    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor a 0")
    private BigDecimal precioVenta;
    
    @Min(value = 0, message = "El stock mínimo debe ser mayor o igual a 0")
    private Integer stockMinimo = 5;
    
    private Long proveedorId;
    private Boolean activo = true;
}