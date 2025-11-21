package com.cesarcort.inventario.configuracion;

import com.cesarcort.inventario.seguridad.DetallesUsuarioServicio;
import com.cesarcort.inventario.seguridad.jwt.JwtFiltro;
import com.cesarcort.inventario.seguridad.jwt.JwtPuntoEntrada;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de Spring Security
 * Tema 8: Seguridad con Spring Security
 * Tema 9: Implementación de autenticación con JWT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class ConfiguracionSeguridad {
    
    private final DetallesUsuarioServicio detallesUsuarioServicio;
    private final PasswordEncoder passwordEncoder;
    private final JwtPuntoEntrada jwtPuntoEntrada;
    private final JwtFiltro jwtFiltro;
    
    /**
     * Configura el proveedor de autenticación
     * Conecta UserDetailsService con el PasswordEncoder
     */
    @Bean
    @SuppressWarnings("deprecation")
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(detallesUsuarioServicio);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    
    /**
     * Bean del AuthenticationManager
     * Necesario para el proceso de autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    /**
     * Configuración principal de seguridad
     * Define qué endpoints requieren autenticación y cuáles son públicos
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF (no necesario para API REST con JWT)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configurar manejo de excepciones
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtPuntoEntrada)
            )
            
            // Configurar política de sesiones (STATELESS para JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configurar autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (sin autenticación)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                
                // Endpoints protegidos por rol (Tema 8)
                .requestMatchers("/api/usuarios/**").hasAnyRole("ADMIN")
                .requestMatchers("/api/categorias/**").hasAnyRole("ADMIN", "VENDEDOR", "ALMACENERO")
                .requestMatchers("/api/proveedores/**").hasAnyRole("ADMIN", "ALMACENERO")
                .requestMatchers("/api/clientes/**").hasAnyRole("ADMIN", "VENDEDOR", "ALMACENERO")
                .requestMatchers("/api/productos/**").hasAnyRole("ADMIN", "VENDEDOR", "ALMACENERO")
                .requestMatchers("/api/inventario/**").hasAnyRole("ADMIN", "ALMACENERO")
                .requestMatchers("/api/movimientos/**").hasAnyRole("ADMIN", "VENDEDOR", "ALMACENERO")
                
                // Todos los demás endpoints requieren autenticación
                .anyRequest().authenticated()
            );
        
        // Configurar proveedor de autenticación
        http.authenticationProvider(authenticationProvider());
        
        // Agregar el filtro JWT antes del filtro de autenticación de Spring
        http.addFilterBefore(jwtFiltro, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}