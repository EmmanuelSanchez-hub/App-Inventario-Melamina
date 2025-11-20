package com.cesarcort.inventario.servicios;

import com.cesarcort.inventario.entidades.Rol;
import com.cesarcort.inventario.entidades.Usuario;
import com.cesarcort.inventario.excepciones.ConflictoException;
import com.cesarcort.inventario.excepciones.RecursoNoEncontradoException;
import com.cesarcort.inventario.repositorios.RolRepositorio;
import com.cesarcort.inventario.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar usuarios del sistema
 */
@Service
@RequiredArgsConstructor
public class UsuarioServicio {
    
    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Obtiene todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return usuarioRepositorio.findAll();
    }
    
    /**
     * Obtiene usuarios activos
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerActivos() {
        return usuarioRepositorio.findByActivo(true);
    }
    
    /**
     * Obtiene un usuario por ID
     */
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepositorio.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", "id", id));
    }
    
    /**
     * Obtiene un usuario por username
     */
    @Transactional(readOnly = true)
    public Usuario obtenerPorUsername(String username) {
        return usuarioRepositorio.findByUsername(username)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", "username", username));
    }
    
    /**
     * Obtiene un usuario por email
     */
    @Transactional(readOnly = true)
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepositorio.findByEmail(email)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", "email", email));
    }
    
    /**
     * Registra un nuevo usuario en el sistema
     */
    @Transactional
    public Usuario registrar(Usuario usuario, String nombreRol) {
        // Validar que el username no exista
        if (usuarioRepositorio.existsByUsername(usuario.getUsername())) {
            throw new ConflictoException("Ya existe un usuario con el username: " + usuario.getUsername());
        }
        
        // Validar que el email no exista
        if (usuarioRepositorio.existsByEmail(usuario.getEmail())) {
            throw new ConflictoException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        
        // Buscar el rol
        Rol rol = rolRepositorio.findByNombre(nombreRol)
            .orElseThrow(() -> new RecursoNoEncontradoException("Rol", "nombre", nombreRol));
        
        // Encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol(rol);
        usuario.setActivo(true);
        
        return usuarioRepositorio.save(usuario);
    }
    
    /**
     * Actualiza un usuario existente (sin cambiar password)
     */
    @Transactional
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        Usuario usuario = obtenerPorId(id);
        
        // Validar username único si se cambió
        if (!usuario.getUsername().equals(usuarioActualizado.getUsername())
            && usuarioRepositorio.existsByUsername(usuarioActualizado.getUsername())) {
            throw new ConflictoException("Ya existe un usuario con el username: " + usuarioActualizado.getUsername());
        }
        
        // Validar email único si se cambió
        if (!usuario.getEmail().equals(usuarioActualizado.getEmail())
            && usuarioRepositorio.existsByEmail(usuarioActualizado.getEmail())) {
            throw new ConflictoException("Ya existe un usuario con el email: " + usuarioActualizado.getEmail());
        }
        
        // Actualizar campos (sin cambiar password ni rol)
        usuario.setUsername(usuarioActualizado.getUsername());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setNombres(usuarioActualizado.getNombres());
        usuario.setApellidos(usuarioActualizado.getApellidos());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setActivo(usuarioActualizado.getActivo());
        
        return usuarioRepositorio.save(usuario);
    }
    
    /**
     * Cambia la contraseña de un usuario
     */
    @Transactional
    public void cambiarPassword(Long id, String nuevaPassword) {
        Usuario usuario = obtenerPorId(id);
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepositorio.save(usuario);
    }
    
    /**
     * Cambia el rol de un usuario
     */
    @Transactional
    public Usuario cambiarRol(Long id, String nombreRol) {
        Usuario usuario = obtenerPorId(id);
        
        Rol rol = rolRepositorio.findByNombre(nombreRol)
            .orElseThrow(() -> new RecursoNoEncontradoException("Rol", "nombre", nombreRol));
        
        usuario.setRol(rol);
        return usuarioRepositorio.save(usuario);
    }
    
    /**
     * Desactiva un usuario
     */
    @Transactional
    public void desactivar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(false);
        usuarioRepositorio.save(usuario);
    }
    
    /**
     * Activa un usuario
     */
    @Transactional
    public void activar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(true);
        usuarioRepositorio.save(usuario);
    }
    
    /**
     * Obtiene usuarios por rol
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerPorRol(Long rolId) {
        return usuarioRepositorio.findByRolId(rolId);
    }
    
    /**
     * Verifica si un usuario existe por username
     */
    @Transactional(readOnly = true)
    public boolean existePorUsername(String username) {
        return usuarioRepositorio.existsByUsername(username);
    }
    
    /**
     * Verifica si un usuario existe por email
     */
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return usuarioRepositorio.existsByEmail(email);
    }
}