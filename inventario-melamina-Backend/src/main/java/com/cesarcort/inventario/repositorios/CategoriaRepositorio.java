package com.cesarcort.inventario.repositorios;

import com.cesarcort.inventario.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Categoria
 * Maneja operaciones de categorías de productos
 */
@Repository
public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
    
    /**
     * Busca una categoría por nombre
     * @param nombre Nombre de la categoría
     * @return Optional con la categoría si existe
     */
    Optional<Categoria> findByNombre(String nombre);
    
    /**
     * Verifica si existe una categoría con el nombre dado
     * @param nombre Nombre de la categoría
     * @return true si existe, false si no
     */
    boolean existsByNombre(String nombre);
    
    /**
     * Busca categorías activas
     * @param activo Estado de la categoría
     * @return Lista de categorías activas
     */
    List<Categoria> findByActivo(Boolean activo);
    
    /**
     * Busca categorías por nombre (búsqueda parcial)
     * @param nombre Parte del nombre a buscar
     * @return Lista de categorías que coincidan
     */
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}