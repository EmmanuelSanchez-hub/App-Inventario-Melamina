package com.cesarcort.inventario.seguridad;

import com.cesarcort.inventario.entidades.Usuario;
import com.cesarcort.inventario.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Implementación de UserDetailsService para Spring Security
 * Carga los detalles del usuario desde la base de datos
 * Tema 8: Seguridad con Spring Security
 */
@Service
@RequiredArgsConstructor
public class DetallesUsuarioServicio implements UserDetailsService {
    
    private final UsuarioRepositorio usuarioRepositorio;
    
    /**
     * Carga un usuario por su username
     * Este método es llamado automáticamente por Spring Security durante la autenticación
     * @param username Username del usuario
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioRepositorio.findByUsername(username)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        // Verificar si el usuario está activo
        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("Usuario desactivado: " + username);
        }
        
        // Convertir el rol del usuario a GrantedAuthority
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(usuario.getRol().getNombre())
        );
        
        // Crear y retornar UserDetails de Spring Security
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.getActivo())
                .build();
    }
    
    /**
     * Carga un usuario por su ID
     * Útil para operaciones internas
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("Usuario no encontrado con ID: " + id));
        
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(usuario.getRol().getNombre())
        );
        
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.getActivo())
                .build();
    }
}