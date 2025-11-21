package com.cesarcort.inventario.controladores;

import com.cesarcort.inventario.dto.mapper.MovimientoMapper;
import com.cesarcort.inventario.dto.request.AjusteInventarioRequest;
import com.cesarcort.inventario.dto.request.MovimientoRequest;
import com.cesarcort.inventario.dto.response.MovimientoResponse;
import com.cesarcort.inventario.entidades.Movimiento;
import com.cesarcort.inventario.entidades.Movimiento.TipoMovimiento;
import com.cesarcort.inventario.servicios.MovimientoServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para movimientos de inventario (entradas/salidas)
 * Accesible por ADMIN, VENDEDOR y ALMACENERO
 */
@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'ALMACENERO')")
public class MovimientoControlador {
    
    private final MovimientoServicio movimientoServicio;
    private final MovimientoMapper movimientoMapper;
    
    /**
     * Obtener todos los movimientos
     * GET /api/movimientos
     */
    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> obtenerTodos() {
        List<Movimiento> movimientos = movimientoServicio.obtenerTodos();
        List<MovimientoResponse> response = movimientos.stream()
            .map(movimientoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener movimiento por ID
     * GET /api/movimientos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> obtenerPorId(@PathVariable Long id) {
        Movimiento movimiento = movimientoServicio.obtenerPorId(id);
        return ResponseEntity.ok(movimientoMapper.toResponse(movimiento));
    }
    
    /**
     * Obtener movimientos por tipo
     * GET /api/movimientos/tipo/{tipo}
     * tipo puede ser: ENTRADA o SALIDA
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimientoResponse>> obtenerPorTipo(@PathVariable String tipo) {
        TipoMovimiento tipoMovimiento = TipoMovimiento.valueOf(tipo.toUpperCase());
        List<Movimiento> movimientos = movimientoServicio.obtenerPorTipo(tipoMovimiento);
        List<MovimientoResponse> response = movimientos.stream()
            .map(movimientoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener movimientos de un producto
     * GET /api/movimientos/producto/{productoId}
     */
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<MovimientoResponse>> obtenerPorProducto(@PathVariable Long productoId) {
        List<Movimiento> movimientos = movimientoServicio.obtenerPorProducto(productoId);
        List<MovimientoResponse> response = movimientos.stream()
            .map(movimientoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener movimientos por rango de fechas
     * GET /api/movimientos/rango?fechaInicio=2024-01-01T00:00:00&fechaFin=2024-12-31T23:59:59
     */
    @GetMapping("/rango")
    public ResponseEntity<List<MovimientoResponse>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        List<Movimiento> movimientos = movimientoServicio.obtenerPorRangoFechas(fechaInicio, fechaFin);
        List<MovimientoResponse> response = movimientos.stream()
            .map(movimientoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Registrar ENTRADA de mercancía (compra)
     * POST /api/movimientos/entrada
     * 
     * Body:
     * {
     *   "productoId": 1,
     *   "cantidad": 50,
     *   "precioUnitario": 85.00,
     *   "proveedorId": 1,
     *   "observaciones": "Compra de tableros"
     * }
     */
    @PostMapping("/entrada")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    public ResponseEntity<MovimientoResponse> registrarEntrada(@Valid @RequestBody MovimientoRequest request) {
        // Obtener el usuario autenticado
        
        // TODO: Obtener el ID del usuario desde el username
        // Por ahora usamos 1 como placeholder (deberías implementar esto)
        Long usuarioId = 1L;
        
        Movimiento movimiento = movimientoServicio.registrarEntrada(
            request.getProductoId(),
            request.getCantidad(),
            request.getPrecioUnitario(),
            request.getProveedorId(),
            usuarioId,
            request.getObservaciones()
        );
        
        log.info("Entrada registrada - Producto: {}, Cantidad: {}", 
            request.getProductoId(), request.getCantidad());
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(movimientoMapper.toResponse(movimiento));
    }
    
    /**
     * Registrar SALIDA de mercancía (venta)
     * POST /api/movimientos/salida
     * 
     * Body:
     * {
     *   "productoId": 1,
     *   "cantidad": 10,
     *   "precioUnitario": 120.00,
     *   "clienteId": 1,
     *   "observaciones": "Venta a cliente"
     * }
     */
    @PostMapping("/salida")
    public ResponseEntity<MovimientoResponse> registrarSalida(@Valid @RequestBody MovimientoRequest request) {
        // Obtener el usuario autenticado
        
        // TODO: Obtener el ID del usuario desde el username
        Long usuarioId = 1L;
        
        Movimiento movimiento = movimientoServicio.registrarSalida(
            request.getProductoId(),
            request.getCantidad(),
            request.getPrecioUnitario(),
            request.getClienteId(),
            usuarioId,
            request.getObservaciones()
        );
        
        log.info("Salida registrada - Producto: {}, Cantidad: {}", 
            request.getProductoId(), request.getCantidad());
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(movimientoMapper.toResponse(movimiento));
    }
    
    /**
     * Registrar ajuste de inventario
     * POST /api/movimientos/ajuste
     * 
     * Body:
     * {
     *   "productoId": 1,
     *   "cantidad": 5,
     *   "tipo": "ENTRADA",
     *   "motivo": "Corrección de inventario",
     *   "observaciones": "Ajuste por conteo físico"
     * }
     */
    @PostMapping("/ajuste")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    public ResponseEntity<MovimientoResponse> registrarAjuste(@Valid @RequestBody AjusteInventarioRequest request) {
        // Obtener el usuario autenticado
        
        // TODO: Obtener el ID del usuario desde el username
        Long usuarioId = 1L;
        
        TipoMovimiento tipo = TipoMovimiento.valueOf(request.getTipo().toUpperCase());
        
        Movimiento movimiento = movimientoServicio.registrarAjuste(
            request.getProductoId(),
            request.getCantidad(),
            tipo,
            usuarioId,
            request.getMotivo(),
            request.getObservaciones()
        );
        
        log.info("Ajuste registrado - Producto: {}, Tipo: {}, Cantidad: {}", 
            request.getProductoId(), tipo, request.getCantidad());
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(movimientoMapper.toResponse(movimiento));
    }
    
    /**
     * Calcular total de ventas en un período
     * GET /api/movimientos/reportes/ventas?fechaInicio=2024-01-01T00:00:00&fechaFin=2024-12-31T23:59:59
     */
    @GetMapping("/reportes/ventas")
    public ResponseEntity<Map<String, Object>> calcularTotalVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        Double totalVentas = movimientoServicio.calcularTotalVentas(fechaInicio, fechaFin);
        
        Map<String, Object> response = new HashMap<>();
        response.put("fechaInicio", fechaInicio);
        response.put("fechaFin", fechaFin);
        response.put("totalVentas", totalVentas);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Calcular total de compras en un período
     * GET /api/movimientos/reportes/compras?fechaInicio=2024-01-01T00:00:00&fechaFin=2024-12-31T23:59:59
     */
    @GetMapping("/reportes/compras")
    public ResponseEntity<Map<String, Object>> calcularTotalCompras(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        Double totalCompras = movimientoServicio.calcularTotalCompras(fechaInicio, fechaFin);
        
        Map<String, Object> response = new HashMap<>();
        response.put("fechaInicio", fechaInicio);
        response.put("fechaFin", fechaFin);
        response.put("totalCompras", totalCompras);
        
        return ResponseEntity.ok(response);
    }
}