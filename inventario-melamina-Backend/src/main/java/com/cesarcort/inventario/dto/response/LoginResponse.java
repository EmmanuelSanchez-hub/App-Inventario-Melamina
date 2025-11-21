package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para login exitoso
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    
    private String token;
    @Builder.Default
    private String tipo = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String nombres;
    private String apellidos;
    private String rol;
}
