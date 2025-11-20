package com.cesarcort.inventario.repositorios;

import com.cesarcort.inventario.entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Proveedor
 * Maneja operaciones de proveedores
 */
@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, Long> {
    
    /**
     * Busca un proveedor por RUC
     * @param ruc RUC del proveedor
     * @return Optional con el proveedor si existe
     */
    Optional<Proveedor> findByRuc(String ruc);
    
    /**
     * Verifica si existe un proveedor con el RUC dado
     * @param ruc RUC del proveedor
     * @return true si existe, false si no
     */
    boolean existsByRuc(String ruc);
    
    /**
     * Busca proveedores activos
     * @param activo Estado del proveedor
     * @return Lista de proveedores activos
     */
    List<Proveedor> findByActivo(Boolean activo);
    
    /**
     * Busca proveedores por razón social (búsqueda parcial)
     * @param razonSocial Parte de la razón social
     * @return Lista de proveedores que coincidan
     */
    List<Proveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);
    
    /**
     * Busca proveedores por ciudad
     * @param ciudad Ciudad del proveedor
     * @return Lista de proveedores de esa ciudad
     */
    List<Proveedor> findByCiudad(String ciudad);
}