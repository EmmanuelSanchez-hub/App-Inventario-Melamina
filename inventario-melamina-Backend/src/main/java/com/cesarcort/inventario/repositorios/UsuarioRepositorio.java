package com.cesarcort.inventario.repositorios;

import com.cesarcort.inventario.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario
 * Maneja operaciones de acceso a datos de usuarios
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su username
     * @param username Nombre de usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByUsername(String username);
    
    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el username dado
     * @param username Nombre de usuario
     * @return true si existe, false si no
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el email dado
     * @param email Email del usuario
     * @return true si existe, false si no
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuarios activos
     * @param activo Estado del usuario
     * @return Lista de usuarios activos
     */
    List<Usuario> findByActivo(Boolean activo);
    
    /**
     * Busca usuarios por rol
     * @param rolId ID del rol
     * @return Lista de usuarios con ese rol
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol.id = :rolId")
    List<Usuario> findByRolId(Long rolId);
}