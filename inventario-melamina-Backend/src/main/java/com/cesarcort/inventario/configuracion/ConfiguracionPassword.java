package com.cesarcort.inventario.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración del PasswordEncoder
 * Se separa en una clase aparte para evitar dependencias circulares
 */
@Configuration
public class ConfiguracionPassword {
    
    /**
     * Bean para encriptar contraseñas con BCrypt
     * Usado por Spring Security y los servicios
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}