package com.cesarcort.inventario.controladores;

import com.cesarcort.inventario.dto.mapper.UsuarioMapper;
import com.cesarcort.inventario.dto.request.CambiarPasswordRequest;
import com.cesarcort.inventario.dto.response.MensajeResponse;
import com.cesarcort.inventario.dto.response.UsuarioResponse;
import com.cesarcort.inventario.entidades.Usuario;
import com.cesarcort.inventario.servicios.UsuarioServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestión de usuarios
 * Solo accesible por ROLE_ADMIN
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioControlador {
    
    private final UsuarioServicio usuarioServicio;
    private final UsuarioMapper usuarioMapper;
    
    /**
     * Obtener todos los usuarios
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        List<Usuario> usuarios = usuarioServicio.obtenerTodos();
        List<UsuarioResponse> response = usuarios.stream()
            .map(usuarioMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener solo usuarios activos
     * GET /api/usuarios/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponse>> obtenerActivos() {
        List<Usuario> usuarios = usuarioServicio.obtenerActivos();
        List<UsuarioResponse> response = usuarios.stream()
            .map(usuarioMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener usuario por ID
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioServicio.obtenerPorId(id);
        return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
    }
    
    /**
     * Obtener usuario por username
     * GET /api/usuarios/username/{username}
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioResponse> obtenerPorUsername(@PathVariable String username) {
        Usuario usuario = usuarioServicio.obtenerPorUsername(username);
        return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
    }
    
    /**
     * Obtener usuarios por rol
     * GET /api/usuarios/rol/{rolId}
     */
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<UsuarioResponse>> obtenerPorRol(@PathVariable Long rolId) {
        List<Usuario> usuarios = usuarioServicio.obtenerPorRol(rolId);
        List<UsuarioResponse> response = usuarios.stream()
            .map(usuarioMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Actualizar usuario (sin cambiar password ni rol)
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuarioActualizado) {
        
        Usuario usuario = usuarioServicio.actualizar(id, usuarioActualizado);
        return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
    }
    
    /**
     * Cambiar contraseña de un usuario
     * PATCH /api/usuarios/{id}/password
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<MensajeResponse> cambiarPassword(
            @PathVariable Long id,
            @Valid @RequestBody CambiarPasswordRequest request) {
        
        usuarioServicio.cambiarPassword(id, request.getNuevaPassword());
        return ResponseEntity.ok(
            MensajeResponse.of("Contraseña actualizada exitosamente")
        );
    }
    
    /**
     * Cambiar rol de un usuario
     * PATCH /api/usuarios/{id}/rol
     * Body: { "rol": "ROLE_VENDEDOR" }
     */
    @PatchMapping("/{id}/rol")
    public ResponseEntity<UsuarioResponse> cambiarRol(
            @PathVariable Long id,
            @RequestBody String nombreRol) {
        
        Usuario usuario = usuarioServicio.cambiarRol(id, nombreRol);
        return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
    }
    
    /**
     * Desactivar usuario
     * PATCH /api/usuarios/{id}/desactivar
     */
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<MensajeResponse> desactivar(@PathVariable Long id) {
        usuarioServicio.desactivar(id);
        return ResponseEntity.ok(
            MensajeResponse.of("Usuario desactivado exitosamente")
        );
    }
    
    /**
     * Activar usuario
     * PATCH /api/usuarios/{id}/activar
     */
    @PatchMapping("/{id}/activar")
    public ResponseEntity<MensajeResponse> activar(@PathVariable Long id) {
        usuarioServicio.activar(id);
        return ResponseEntity.ok(
            MensajeResponse.of("Usuario activado exitosamente")
        );
    }
}