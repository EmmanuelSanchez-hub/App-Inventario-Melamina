package com.cesarcort.inventario.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear/actualizar clientes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {
    
    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento; // DNI, RUC, CE
    
    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 20, message = "El número de documento no puede exceder 20 caracteres")
    private String numeroDocumento;
    
    private String nombres;
    private String apellidos;
    private String razonSocial;
    private String telefono;
    
    @Email(message = "El email debe ser válido")
    private String email;
    
    private String direccion;
    private String ciudad;
    private Boolean activo = true;
}