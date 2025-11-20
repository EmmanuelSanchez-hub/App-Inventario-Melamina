package com.cesarcort.inventario.servicios;

import com.cesarcort.inventario.entidades.Categoria;
import com.cesarcort.inventario.excepciones.ConflictoException;
import com.cesarcort.inventario.excepciones.RecursoNoEncontradoException;
import com.cesarcort.inventario.repositorios.CategoriaRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar categorías de productos
 */
@Service
@RequiredArgsConstructor
public class CategoriaServicio {
    
    private final CategoriaRepositorio categoriaRepositorio;
    
    /**
     * Obtiene todas las categorías
     */
    @Transactional(readOnly = true)
    public List<Categoria> obtenerTodas() {
        return categoriaRepositorio.findAll();
    }
    
    /**
     * Obtiene categorías activas
     */
    @Transactional(readOnly = true)
    public List<Categoria> obtenerActivas() {
        return categoriaRepositorio.findByActivo(true);
    }
    
    /**
     * Obtiene una categoría por ID
     */
    @Transactional(readOnly = true)
    public Categoria obtenerPorId(Long id) {
        return categoriaRepositorio.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Categoria", "id", id));
    }
    
    /**
     * Crea una nueva categoría
     */
    @Transactional
    public Categoria crear(Categoria categoria) {
        // Validar que no exista una categoría con el mismo nombre
        if (categoriaRepositorio.existsByNombre(categoria.getNombre())) {
            throw new ConflictoException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaRepositorio.save(categoria);
    }
    
    /**
     * Actualiza una categoría existente
     */
    @Transactional
    public Categoria actualizar(Long id, Categoria categoriaActualizada) {
        Categoria categoria = obtenerPorId(id);
        
        // Validar nombre único si se cambió
        if (!categoria.getNombre().equals(categoriaActualizada.getNombre()) 
            && categoriaRepositorio.existsByNombre(categoriaActualizada.getNombre())) {
            throw new ConflictoException("Ya existe una categoría con el nombre: " + categoriaActualizada.getNombre());
        }
        
        categoria.setNombre(categoriaActualizada.getNombre());
        categoria.setDescripcion(categoriaActualizada.getDescripcion());
        categoria.setActivo(categoriaActualizada.getActivo());
        
        return categoriaRepositorio.save(categoria);
    }
    
    /**
     * Elimina (desactiva) una categoría
     */
    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = obtenerPorId(id);
        categoria.setActivo(false);
        categoriaRepositorio.save(categoria);
    }
    
    /**
     * Busca categorías por nombre
     */
    @Transactional(readOnly = true)
    public List<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepositorio.findByNombreContainingIgnoreCase(nombre);
    }
}
