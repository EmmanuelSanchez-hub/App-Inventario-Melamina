package com.cesarcort.inventario.repositorios;

import com.cesarcort.inventario.entidades.Movimiento;
import com.cesarcort.inventario.entidades.Movimiento.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Movimiento
 * Maneja operaciones de movimientos de inventario (entradas/salidas)
 */
@Repository
public interface MovimientoRepositorio extends JpaRepository<Movimiento, Long> {
    
    /**
     * Busca movimientos por tipo (ENTRADA o SALIDA)
     * @param tipoMovimiento Tipo de movimiento
     * @return Lista de movimientos de ese tipo
     */
    List<Movimiento> findByTipoMovimiento(TipoMovimiento tipoMovimiento);
    
    /**
     * Busca movimientos por producto
     * @param productoId ID del producto
     * @return Lista de movimientos de ese producto
     */
    @Query("SELECT m FROM Movimiento m WHERE m.producto.id = :productoId ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findByProductoId(@Param("productoId") Long productoId);
    
    /**
     * Busca movimientos por usuario
     * @param usuarioId ID del usuario
     * @return Lista de movimientos registrados por ese usuario
     */
    @Query("SELECT m FROM Movimiento m WHERE m.usuario.id = :usuarioId ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Busca movimientos por proveedor
     * @param proveedorId ID del proveedor
     * @return Lista de movimientos relacionados a ese proveedor
     */
    @Query("SELECT m FROM Movimiento m WHERE m.proveedor.id = :proveedorId ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findByProveedorId(@Param("proveedorId") Long proveedorId);
    
    /**
     * Busca movimientos por cliente
     * @param clienteId ID del cliente
     * @return Lista de movimientos relacionados a ese cliente
     */
    @Query("SELECT m FROM Movimiento m WHERE m.cliente.id = :clienteId ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findByClienteId(@Param("clienteId") Long clienteId);
    
    /**
     * Busca movimientos en un rango de fechas
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de movimientos en ese rango
     */
    @Query("SELECT m FROM Movimiento m WHERE m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findByFechaMovimientoBetween(
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Busca los últimos N movimientos
     * @param limite Cantidad de movimientos a obtener
     * @return Lista de los últimos movimientos
     */
    @Query("SELECT m FROM Movimiento m ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findUltimosMovimientos();
    
    /**
     * Calcula el total de ventas en un período
     */
    @Query("SELECT SUM(m.total) FROM Movimiento m WHERE m.tipoMovimiento = 'SALIDA' " +
           "AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalVentas(
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Calcula el total de compras en un período
     */
    @Query("SELECT SUM(m.total) FROM Movimiento m WHERE m.tipoMovimiento = 'ENTRADA' " +
           "AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalCompras(
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin);
}