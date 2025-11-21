package com.cesarcort.inventario.configuracion;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Propiedades de configuración JWT
 * Mapea los valores de jwt.* del archivo application.properties
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    
    /**
     * Clave secreta para firmar los tokens JWT (en Base64)
     */
    private String secret;
    
    /**
     * Tiempo de expiración del token en milisegundos (24 horas = 86400000)
     */
    private long expiration;
}
