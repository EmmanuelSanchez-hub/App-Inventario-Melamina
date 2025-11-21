package com.cesarcort.inventario.servicios;

import com.cesarcort.inventario.entidades.Inventario;
import com.cesarcort.inventario.excepciones.RecursoNoEncontradoException;
import com.cesarcort.inventario.repositorios.InventarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar el inventario de productos
 */
@Service
@RequiredArgsConstructor
public class InventarioServicio {
    
    private final InventarioRepositorio inventarioRepositorio;
    
    /**
     * Obtiene todo el inventario
     */
    @Transactional(readOnly = true)
    public List<Inventario> obtenerTodo() {
        return inventarioRepositorio.findAll();
    }
    
    /**
     * Obtiene inventario por ID
     */
    @Transactional(readOnly = true)
    public Inventario obtenerPorId(Long id) {
        return inventarioRepositorio.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Inventario", "id", id));
    }
    
    /**
     * Obtiene inventario de un producto específico
     */
    @Transactional(readOnly = true)
    public Inventario obtenerPorProductoId(Long productoId) {
        return inventarioRepositorio.findByProductoId(productoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Inventario", "productoId", productoId));
    }
    
    /**
     * Obtiene productos con stock disponible
     */
    @Transactional(readOnly = true)
    public List<Inventario> obtenerConStock() {
        return inventarioRepositorio.findInventariosConStock();
    }
    
    /**
     * Obtiene productos sin stock
     */
    @Transactional(readOnly = true)
    public List<Inventario> obtenerSinStock() {
        return inventarioRepositorio.findInventariosSinStock();
    }
    
    /**
     * Calcula el valor total del inventario
     */
    @Transactional(readOnly = true)
    public Double calcularValorTotal() {
        Double valor = inventarioRepositorio.calcularValorTotalInventario();
        return valor != null ? valor : 0.0;
    }
    
    /**
     * Actualiza la ubicación de un producto en el almacén
     */
    @Transactional
    public Inventario actualizarUbicacion(Long productoId, String nuevaUbicacion) {
        Inventario inventario = obtenerPorProductoId(productoId);
        inventario.setUbicacion(nuevaUbicacion);
        return inventarioRepositorio.save(inventario);
    }
    
    /**
     * Obtiene el stock actual de un producto
     */
    @Transactional(readOnly = true)
    public Integer obtenerStockActual(Long productoId) {
        Inventario inventario = obtenerPorProductoId(productoId);
        return inventario.getCantidadActual();
    }
    
    /**
     * Verifica si hay suficiente stock
     */
    @Transactional(readOnly = true)
    public boolean hayStockSuficiente(Long productoId, Integer cantidadRequerida) {
        Integer stockActual = obtenerStockActual(productoId);
        return stockActual >= cantidadRequerida;
    }
    
    /**
     * Actualiza manualmente el stock (usar con precaución)
     * Normalmente el stock se actualiza automáticamente con movimientos
     */
    @Transactional
    public Inventario ajustarStock(Long productoId, Integer nuevaCantidad) {
        Inventario inventario = obtenerPorProductoId(productoId);
        inventario.setCantidadActual(nuevaCantidad);
        return inventarioRepositorio.save(inventario);
    }
}
