package com.cesarcort.inventario.repositorios;

import com.cesarcort.inventario.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Cliente
 * Maneja operaciones de clientes
 */
@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca un cliente por número de documento
     * @param numeroDocumento Número de documento del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);
    
    /**
     * Verifica si existe un cliente con el número de documento dado
     * @param numeroDocumento Número de documento
     * @return true si existe, false si no
     */
    boolean existsByNumeroDocumento(String numeroDocumento);
    
    /**
     * Busca clientes activos
     * @param activo Estado del cliente
     * @return Lista de clientes activos
     */
    List<Cliente> findByActivo(Boolean activo);
    
    /**
     * Busca clientes por tipo de documento
     * @param tipoDocumento Tipo de documento (DNI, RUC, CE)
     * @return Lista de clientes con ese tipo de documento
     */
    List<Cliente> findByTipoDocumento(String tipoDocumento);
    
    /**
     * Busca clientes por nombre o apellido (búsqueda parcial)
     * @param termino Término de búsqueda
     * @return Lista de clientes que coincidan
     */
    List<Cliente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
        String nombres, String apellidos);
    
    /**
     * Busca clientes por razón social (búsqueda parcial)
     * @param razonSocial Parte de la razón social
     * @return Lista de clientes que coincidan
     */
    List<Cliente> findByRazonSocialContainingIgnoreCase(String razonSocial);
    
    /**
     * Busca clientes por ciudad
     * @param ciudad Ciudad del cliente
     * @return Lista de clientes de esa ciudad
     */
    List<Cliente> findByCiudad(String ciudad);
}