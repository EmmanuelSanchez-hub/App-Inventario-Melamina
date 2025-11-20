package com.cesarcort.inventario.repositorios;

import com.cesarcort.inventario.entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Rol
 * Maneja las operaciones de acceso a datos de roles
 */
@Repository
public interface RolRepositorio extends JpaRepository<Rol, Long> {
    
    /**
     * Busca un rol por su nombre
     * @param nombre Nombre del rol (ej: ROLE_ADMIN)
     * @return Optional con el rol si existe
     */
    Optional<Rol> findByNombre(String nombre);
    
    /**
     * Verifica si existe un rol con el nombre dado
     * @param nombre Nombre del rol
     * @return true si existe, false si no
     */
    boolean existsByNombre(String nombre);
}