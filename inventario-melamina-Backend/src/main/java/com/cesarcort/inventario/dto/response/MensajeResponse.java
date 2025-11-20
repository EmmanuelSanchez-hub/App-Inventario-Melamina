package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico para mensajes de respuesta
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeResponse {
    
    private String mensaje;
    
    public static MensajeResponse of(String mensaje) {
        return new MensajeResponse(mensaje);
    }
}