package com.cesarcort.inventario.servicios;

import com.cesarcort.inventario.entidades.*;
import com.cesarcort.inventario.entidades.Movimiento.TipoMovimiento;
import com.cesarcort.inventario.excepciones.RecursoNoEncontradoException;
import com.cesarcort.inventario.excepciones.StockInsuficienteException;
import com.cesarcort.inventario.repositorios.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestionar movimientos de inventario (entradas y salidas)
 * IMPORTANTE: Los triggers de la BD actualizan automáticamente el inventario
 */
@Service
@RequiredArgsConstructor
public class MovimientoServicio {
    
    private final MovimientoRepositorio movimientoRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final ProveedorRepositorio proveedorRepositorio;
    private final ClienteRepositorio clienteRepositorio;
    private final InventarioRepositorio inventarioRepositorio;
    
    /**
     * Obtiene todos los movimientos
     */
    @Transactional(readOnly = true)
    public List<Movimiento> obtenerTodos() {
        return movimientoRepositorio.findAll();
    }
    
    /**
     * Obtiene un movimiento por ID
     */
    @Transactional(readOnly = true)
    public Movimiento obtenerPorId(Long id) {
        return movimientoRepositorio.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Movimiento", "id", id));
    }
    
    /**
     * Obtiene movimientos por tipo (ENTRADA o SALIDA)
     */
    @Transactional(readOnly = true)
    public List<Movimiento> obtenerPorTipo(TipoMovimiento tipo) {
        return movimientoRepositorio.findByTipoMovimiento(tipo);
    }
    
    /**
     * Obtiene movimientos de un producto
     */
    @Transactional(readOnly = true)
    public List<Movimiento> obtenerPorProducto(Long productoId) {
        return movimientoRepositorio.findByProductoId(productoId);
    }
    
    /**
     * Obtiene movimientos de un usuario
     */
    @Transactional(readOnly = true)
    public List<Movimiento> obtenerPorUsuario(Long usuarioId) {
        return movimientoRepositorio.findByUsuarioId(usuarioId);
    }
    
    /**
     * Registra una ENTRADA de mercancía (compra a proveedor)
     */
    @Transactional
    public Movimiento registrarEntrada(Long productoId, Integer cantidad, 
                                      BigDecimal precioUnitario, Long proveedorId, 
                                      Long usuarioId, String observaciones) {
        
        // Validar que el producto existe
        Producto producto = productoRepositorio.findById(productoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Producto", "id", productoId));
        
        // Validar que el usuario existe
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", "id", usuarioId));
        
        // Validar proveedor si se proporciona
        Proveedor proveedor = null;
        if (proveedorId != null) {
            proveedor = proveedorRepositorio.findById(proveedorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor", "id", proveedorId));
        }
        
        // Calcular total
        BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        
        // Crear movimiento
        Movimiento movimiento = Movimiento.builder()
            .tipoMovimiento(TipoMovimiento.ENTRADA)
            .producto(producto)
            .cantidad(cantidad)
            .precioUnitario(precioUnitario)
            .total(total)
            .motivo("Compra")
            .proveedor(proveedor)
            .usuario(usuario)
            .observaciones(observaciones)
            .build();
        
        // Guardar movimiento (el trigger actualizará el inventario automáticamente)
        return movimientoRepositorio.save(movimiento);
    }
    
    /**
     * Registra una SALIDA de mercancía (venta a cliente)
     */
    @Transactional
    public Movimiento registrarSalida(Long productoId, Integer cantidad, 
                                     BigDecimal precioUnitario, Long clienteId, 
                                     Long usuarioId, String observaciones) {
        
        // Validar que el producto existe
        Producto producto = productoRepositorio.findById(productoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Producto", "id", productoId));
        
        // Validar que hay suficiente stock
        Inventario inventario = inventarioRepositorio.findByProductoId(productoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Inventario", "productoId", productoId));
        
        if (inventario.getCantidadActual() < cantidad) {
            throw new StockInsuficienteException(
                producto.getNombre(), 
                inventario.getCantidadActual(), 
                cantidad
            );
        }
        
        // Validar que el usuario existe
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", "id", usuarioId));
        
        // Validar cliente si se proporciona
        Cliente cliente = null;
        if (clienteId != null) {
            cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", "id", clienteId));
        }
        
        // Calcular total
        BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        
        // Crear movimiento
        Movimiento movimiento = Movimiento.builder()
            .tipoMovimiento(TipoMovimiento.SALIDA)
            .producto(producto)
            .cantidad(cantidad)
            .precioUnitario(precioUnitario)
            .total(total)
            .motivo("Venta")
            .cliente(cliente)
            .usuario(usuario)
            .observaciones(observaciones)
            .build();
        
        // Guardar movimiento (el trigger actualizará el inventario automáticamente)
        return movimientoRepositorio.save(movimiento);
    }
    
    /**
     * Registra un ajuste de inventario (corrección manual)
     */
    @Transactional
    public Movimiento registrarAjuste(Long productoId, Integer cantidad, 
                                     TipoMovimiento tipo, Long usuarioId, 
                                     String motivo, String observaciones) {
        
        Producto producto = productoRepositorio.findById(productoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Producto", "id", productoId));
        
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", "id", usuarioId));
        
        // Si es SALIDA, validar stock
        if (tipo == TipoMovimiento.SALIDA) {
            Inventario inventario = inventarioRepositorio.findByProductoId(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inventario", "productoId", productoId));
            
            if (inventario.getCantidadActual() < cantidad) {
                throw new StockInsuficienteException(
                    producto.getNombre(), 
                    inventario.getCantidadActual(), 
                    cantidad
                );
            }
        }
        
        Movimiento movimiento = Movimiento.builder()
            .tipoMovimiento(tipo)
            .producto(producto)
            .cantidad(cantidad)
            .motivo(motivo)
            .usuario(usuario)
            .observaciones(observaciones)
            .build();
        
        return movimientoRepositorio.save(movimiento);
    }
    
    /**
     * Obtiene movimientos en un rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Movimiento> obtenerPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepositorio.findByFechaMovimientoBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Calcula total de ventas en un período
     */
    @Transactional(readOnly = true)
    public Double calcularTotalVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Double total = movimientoRepositorio.calcularTotalVentas(fechaInicio, fechaFin);
        return total != null ? total : 0.0;
    }
    
    /**
     * Calcula total de compras en un período
     */
    @Transactional(readOnly = true)
    public Double calcularTotalCompras(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Double total = movimientoRepositorio.calcularTotalCompras(fechaInicio, fechaFin);
        return total != null ? total : 0.0;
    }
}