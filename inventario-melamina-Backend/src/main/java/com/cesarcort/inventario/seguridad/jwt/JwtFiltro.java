package com.cesarcort.inventario.seguridad.jwt;

import com.cesarcort.inventario.seguridad.DetallesUsuarioServicio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta cada petición HTTP para validar el token JWT
 * Se ejecuta una vez por cada petición (OncePerRequestFilter)
 * Tema 9: Implementación de autenticación con JWT
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFiltro extends OncePerRequestFilter {
    
    private final JwtServicio jwtServicio;
    private final DetallesUsuarioServicio detallesUsuarioServicio;
    
    /**
     * Método principal del filtro que se ejecuta en cada petición
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // 1. Extraer el token JWT del header Authorization
            String jwt = obtenerJwtDelRequest(request);
            
            // 2. Validar que el token exista y sea válido
            if (StringUtils.hasText(jwt) && jwtServicio.validarToken(jwt)) {
                
                // 3. Obtener el username del token
                String username = jwtServicio.obtenerUsernameDelToken(jwt);
                
                // 4. Cargar los detalles del usuario desde la base de datos
                UserDetails userDetails = detallesUsuarioServicio.loadUserByUsername(username);
                
                // 5. Crear el objeto de autenticación de Spring Security
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()
                    );
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 6. Establecer la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("Usuario autenticado: {}", username);
            }
        } catch (Exception ex) {
            log.error("No se pudo establecer la autenticación del usuario: {}", ex.getMessage());
        }
        
        // 7. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
    
    /**
     * Extrae el token JWT del header Authorization
     * Formato esperado: "Bearer <token>"
     * @param request HttpServletRequest
     * @return Token JWT sin el prefijo "Bearer "
     */
    private String obtenerJwtDelRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        // Verificar que el header existe y comienza con "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remover "Bearer " del inicio
        }
        
        return null;
    }
}