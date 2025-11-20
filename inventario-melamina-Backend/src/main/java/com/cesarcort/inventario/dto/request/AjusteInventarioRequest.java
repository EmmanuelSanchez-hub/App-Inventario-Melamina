package com.cesarcort.inventario.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para ajustes manuales de inventario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjusteInventarioRequest {
    
    @NotNull(message = "El producto es obligatorio")
    private Long productoId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @NotBlank(message = "El tipo es obligatorio")
    private String tipo; // ENTRADA o SALIDA
    
    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;
    
    private String observaciones;
}