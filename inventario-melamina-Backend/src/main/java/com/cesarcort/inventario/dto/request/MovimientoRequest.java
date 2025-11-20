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
 * DTO para registrar movimientos de inventario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoRequest {
    
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento; // ENTRADA o SALIDA
    
    @NotNull(message = "El producto es obligatorio")
    private Long productoId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor a 0")
    private BigDecimal precioUnitario;
    
    private String motivo;
    private Long proveedorId; // Para entradas
    private Long clienteId;   // Para salidas
    private String observaciones;
}
