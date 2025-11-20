package com.cesarcort.inventario.controladores;

import com.cesarcort.inventario.dto.response.MensajeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de pruebas para verificar la configuración de seguridad
 * Endpoints para probar autenticación y autorización por roles
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestControlador {
    
    /**
     * Endpoint público - NO requiere autenticación
     * GET /api/test/publico
     */
    @GetMapping("/publico")
    public ResponseEntity<?> endpointPublico() {
        return ResponseEntity.ok(
            MensajeResponse.of("✅ Este es un endpoint PÚBLICO - Sin autenticación requerida")
        );
    }
    
    /**
     * Endpoint protegido - Requiere autenticación (cualquier rol)
     * GET /api/test/usuario
     * Header: Authorization: Bearer <token>
     */
    @GetMapping("/usuario")
    public ResponseEntity<?> endpointUsuario() {
        return ResponseEntity.ok(
            MensajeResponse.of("✅ Acceso autorizado - Usuario autenticado correctamente")
        );
    }
    
    /**
     * Endpoint solo para ADMIN
     * GET /api/test/admin
     * Header: Authorization: Bearer <token>
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> endpointAdmin() {
        return ResponseEntity.ok(
            MensajeResponse.of("✅ Acceso autorizado - ROLE_ADMIN verificado")
        );
    }
    
    /**
     * Endpoint para VENDEDOR o ADMIN
     * GET /api/test/vendedor
     * Header: Authorization: Bearer <token>
     */
    @GetMapping("/vendedor")
    @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
    public ResponseEntity<?> endpointVendedor() {
        return ResponseEntity.ok(
            MensajeResponse.of("✅ Acceso autorizado - ROLE_VENDEDOR o ROLE_ADMIN verificado")
        );
    }
    
    /**
     * Endpoint para ALMACENERO o ADMIN
     * GET /api/test/almacenero
     * Header: Authorization: Bearer <token>
     */
    @GetMapping("/almacenero")
    @PreAuthorize("hasAnyRole('ALMACENERO', 'ADMIN')")
    public ResponseEntity<?> endpointAlmacenero() {
        return ResponseEntity.ok(
            MensajeResponse.of("✅ Acceso autorizado - ROLE_ALMACENERO o ROLE_ADMIN verificado")
        );
    }
    
    /**
     * Endpoint para todos los roles
     * GET /api/test/todos
     * Header: Authorization: Bearer <token>
     */
    @GetMapping("/todos")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'ALMACENERO')")
    public ResponseEntity<?> endpointTodos() {
        return ResponseEntity.ok(
            MensajeResponse.of("✅ Acceso autorizado - Todos los roles pueden acceder")
        );
    }
}