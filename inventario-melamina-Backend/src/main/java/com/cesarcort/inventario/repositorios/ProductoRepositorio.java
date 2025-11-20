package com.cesarcort.inventario.repositorios;

import com.cesarcort.inventario.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Producto
 * Maneja operaciones de productos de melamina/madera
 */
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    
    /**
     * Busca un producto por código
     * @param codigo Código del producto
     * @return Optional con el producto si existe
     */
    Optional<Producto> findByCodigo(String codigo);
    
    /**
     * Verifica si existe un producto con el código dado
     * @param codigo Código del producto
     * @return true si existe, false si no
     */
    boolean existsByCodigo(String codigo);
    
    /**
     * Busca productos activos
     * @param activo Estado del producto
     * @return Lista de productos activos
     */
    List<Producto> findByActivo(Boolean activo);
    
    /**
     * Busca productos por categoría
     * @param categoriaId ID de la categoría
     * @return Lista de productos de esa categoría
     */
    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.activo = true")
    List<Producto> findByCategoriaId(@Param("categoriaId") Long categoriaId);
    
    /**
     * Busca productos por proveedor
     * @param proveedorId ID del proveedor
     * @return Lista de productos de ese proveedor
     */
    @Query("SELECT p FROM Producto p WHERE p.proveedor.id = :proveedorId AND p.activo = true")
    List<Producto> findByProveedorId(@Param("proveedorId") Long proveedorId);
    
    /**
     * Busca productos por nombre (búsqueda parcial)
     * @param nombre Parte del nombre a buscar
     * @return Lista de productos que coincidan
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Busca productos por color
     * @param color Color del producto
     * @return Lista de productos de ese color
     */
    List<Producto> findByColorIgnoreCase(String color);
    
    /**
     * Busca productos con stock bajo
     * JPQL para encontrar productos donde el stock actual <= stock mínimo
     */
    @Query("SELECT p FROM Producto p JOIN p.inventario i WHERE i.cantidadActual <= p.stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo();
    
    /**
     * Busca productos sin stock
     */
    @Query("SELECT p FROM Producto p JOIN p.inventario i WHERE i.cantidadActual = 0 AND p.activo = true")
    List<Producto> findProductosSinStock();
}