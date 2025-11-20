package com.cesarcort.inventario.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear/actualizar proveedores
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorRequest {
    
    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;
    
    @Size(max = 20, message = "El RUC no puede exceder 20 caracteres")
    private String ruc;
    
    private String contacto;
    private String telefono;
    
    @Email(message = "El email debe ser válido")
    private String email;
    
    private String direccion;
    private String ciudad;
    private Boolean activo = true;
}