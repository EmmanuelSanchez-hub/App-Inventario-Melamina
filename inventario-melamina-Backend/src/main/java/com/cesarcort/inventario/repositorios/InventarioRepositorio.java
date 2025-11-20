package com.cesarcort.inventario.repositorios;

import com.cesarcort.inventario.entidades.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Inventario
 * Maneja operaciones de stock de productos
 */
@Repository
public interface InventarioRepositorio extends JpaRepository<Inventario, Long> {
    
    /**
     * Busca inventario por producto
     * @param productoId ID del producto
     * @return Optional con el inventario si existe
     */
    @Query("SELECT i FROM Inventario i WHERE i.producto.id = :productoId")
    Optional<Inventario> findByProductoId(Long productoId);
    
    /**
     * Busca inventarios por ubicación
     * @param ubicacion Ubicación en el almacén
     * @return Lista de inventarios en esa ubicación
     */
    List<Inventario> findByUbicacion(String ubicacion);
    
    /**
     * Busca productos con cantidad mayor a cero
     */
    @Query("SELECT i FROM Inventario i WHERE i.cantidadActual > 0")
    List<Inventario> findInventariosConStock();
    
    /**
     * Busca productos sin stock
     */
    @Query("SELECT i FROM Inventario i WHERE i.cantidadActual = 0")
    List<Inventario> findInventariosSinStock();
    
    /**
     * Calcula el valor total del inventario
     */
    @Query("SELECT SUM(i.cantidadActual * p.precioCompra) FROM Inventario i JOIN i.producto p WHERE p.activo = true")
    Double calcularValorTotalInventario();
}
