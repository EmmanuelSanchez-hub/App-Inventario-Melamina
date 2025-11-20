package com.cesarcort.inventario.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para cambiar contraseña de usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarPasswordRequest {
    
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    private String nuevaPassword;
}