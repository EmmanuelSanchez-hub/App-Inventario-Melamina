package com.cesarcort.inventario.controladores;

import com.cesarcort.inventario.dto.mapper.UsuarioMapper;
import com.cesarcort.inventario.dto.request.LoginRequest;
import com.cesarcort.inventario.dto.request.RegistroRequest;
import com.cesarcort.inventario.dto.response.LoginResponse;
import com.cesarcort.inventario.dto.response.MensajeResponse;
import com.cesarcort.inventario.dto.response.UsuarioResponse;
import com.cesarcort.inventario.entidades.Usuario;
import com.cesarcort.inventario.seguridad.jwt.JwtServicio;
import com.cesarcort.inventario.servicios.UsuarioServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para autenticación y registro de usuarios
 * Endpoints públicos para login y registro
 * Tema 9: Implementación de autenticación con JWT
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AutenticacionControlador {
    
    private final AuthenticationManager authenticationManager;
    private final UsuarioServicio usuarioServicio;
    private final JwtServicio jwtServicio;
    private final UsuarioMapper usuarioMapper;
    
    /**
     * Endpoint de login
     * POST /api/auth/login
     * 
     * Body:
     * {
     *   "username": "admin",
     *   "password": "admin"
     * }
     * 
     * Response:
     * {
     *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *   "tipo": "Bearer",
     *   "id": 1,
     *   "username": "admin",
     *   "email": "admin@cesarcort.com",
     *   "rol": "ROLE_ADMIN"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 1. Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            // 2. Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 3. Generar el token JWT
            String jwt = jwtServicio.generarToken(authentication);
            
            // 4. Obtener los detalles del usuario
            Usuario usuario = usuarioServicio.obtenerPorUsername(loginRequest.getUsername());
            
            // 5. Construir la respuesta
            LoginResponse loginResponse = LoginResponse.builder()
                .token(jwt)
                .tipo("Bearer")
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .rol(usuario.getRol().getNombre())
                .build();
            
            log.info("Usuario autenticado exitosamente: {}", usuario.getUsername());
            
            return ResponseEntity.ok(loginResponse);
            
        } catch (Exception e) {
            log.error("Error en autenticación: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(MensajeResponse.of("Credenciales inválidas"));
        }
    }
    
    /**
     * Endpoint de registro de nuevos usuarios
     * POST /api/auth/registro
     * 
     * Body:
     * {
     *   "username": "vendedor2",
     *   "email": "vendedor2@cesarcort.com",
     *   "password": "vende123",
     *   "nombres": "Carlos",
     *   "apellidos": "López",
     *   "telefono": "987654321",
     *   "rol": "ROLE_VENDEDOR"
     * }
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest registroRequest) {
        try {
            // Verificar si el username ya existe
            if (usuarioServicio.existePorUsername(registroRequest.getUsername())) {
                return ResponseEntity
                    .badRequest()
                    .body(MensajeResponse.of("El username ya está en uso"));
            }
            
            // Verificar si el email ya existe
            if (usuarioServicio.existePorEmail(registroRequest.getEmail())) {
                return ResponseEntity
                    .badRequest()
                    .body(MensajeResponse.of("El email ya está en uso"));
            }
            
            // Crear el usuario
            Usuario nuevoUsuario = Usuario.builder()
                .username(registroRequest.getUsername())
                .email(registroRequest.getEmail())
                .password(registroRequest.getPassword())
                .nombres(registroRequest.getNombres())
                .apellidos(registroRequest.getApellidos())
                .telefono(registroRequest.getTelefono())
                .activo(true)
                .build();
            
            // Registrar el usuario (la contraseña se hashea en el servicio)
            Usuario usuarioGuardado = usuarioServicio.registrar(nuevoUsuario, registroRequest.getRol());
            
            // Convertir a DTO de respuesta
            UsuarioResponse response = usuarioMapper.toResponse(usuarioGuardado);
            
            log.info("Usuario registrado exitosamente: {}", usuarioGuardado.getUsername());
            
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
            
        } catch (Exception e) {
            log.error("Error en registro: {}", e.getMessage());
            return ResponseEntity
                .badRequest()
                .body(MensajeResponse.of("Error al registrar usuario: " + e.getMessage()));
        }
    }
    
    /**
     * Endpoint para obtener el usuario actual autenticado
     * GET /api/auth/me
     * Requiere: Header Authorization: Bearer <token>
     */
    @GetMapping("/me")
    public ResponseEntity<?> obtenerUsuarioActual() {
        try {
            // Obtener el usuario autenticado del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Obtener el usuario completo de la BD
            Usuario usuario = usuarioServicio.obtenerPorUsername(userDetails.getUsername());
            
            // Convertir a DTO y retornar
            return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
            
        } catch (Exception e) {
            log.error("Error al obtener usuario actual: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(MensajeResponse.of("No autenticado"));
        }
    }
    
    /**
     * Endpoint de prueba público
     * GET /api/auth/test
     */
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(MensajeResponse.of("API de autenticación funcionando correctamente"));
    }
}