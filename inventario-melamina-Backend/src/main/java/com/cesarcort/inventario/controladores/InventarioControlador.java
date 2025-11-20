package com.cesarcort.inventario.controladores;

import com.cesarcort.inventario.dto.mapper.InventarioMapper;
import com.cesarcort.inventario.dto.response.InventarioResponse;
import com.cesarcort.inventario.dto.response.MensajeResponse;
import com.cesarcort.inventario.entidades.Inventario;
import com.cesarcort.inventario.servicios.InventarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para consultas de inventario
 * Accesible por ADMIN y ALMACENERO
 */
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
public class InventarioControlador {
    
    private final InventarioServicio inventarioServicio;
    private final InventarioMapper inventarioMapper;
    
    /**
     * Obtener todo el inventario
     * GET /api/inventario
     */
    @GetMapping
    public ResponseEntity<List<InventarioResponse>> obtenerTodo() {
        List<Inventario> inventarios = inventarioServicio.obtenerTodo();
        List<InventarioResponse> response = inventarios.stream()
            .map(inventarioMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener inventario por ID
     * GET /api/inventario/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponse> obtenerPorId(@PathVariable Long id) {
        Inventario inventario = inventarioServicio.obtenerPorId(id);
        return ResponseEntity.ok(inventarioMapper.toResponse(inventario));
    }
    
    /**
     * Obtener inventario de un producto específico
     * GET /api/inventario/producto/{productoId}
     */
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioResponse> obtenerPorProductoId(@PathVariable Long productoId) {
        Inventario inventario = inventarioServicio.obtenerPorProductoId(productoId);
        return ResponseEntity.ok(inventarioMapper.toResponse(inventario));
    }
    
    /**
     * Obtener productos con stock disponible
     * GET /api/inventario/con-stock
     */
    @GetMapping("/con-stock")
    public ResponseEntity<List<InventarioResponse>> obtenerConStock() {
        List<Inventario> inventarios = inventarioServicio.obtenerConStock();
        List<InventarioResponse> response = inventarios.stream()
            .map(inventarioMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener productos sin stock
     * GET /api/inventario/sin-stock
     */
    @GetMapping("/sin-stock")
    public ResponseEntity<List<InventarioResponse>> obtenerSinStock() {
        List<Inventario> inventarios = inventarioServicio.obtenerSinStock();
        List<InventarioResponse> response = inventarios.stream()
            .map(inventarioMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Calcular valor total del inventario
     * GET /api/inventario/valor-total
     */
    @GetMapping("/valor-total")
    public ResponseEntity<Map<String, Object>> calcularValorTotal() {
        Double valorTotal = inventarioServicio.calcularValorTotal();
        Map<String, Object> response = new HashMap<>();
        response.put("valorTotal", valorTotal);
        response.put("mensaje", "Valor total del inventario calculado");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener stock actual de un producto
     * GET /api/inventario/stock/{productoId}
     */
    @GetMapping("/stock/{productoId}")
    public ResponseEntity<Map<String, Object>> obtenerStockActual(@PathVariable Long productoId) {
        Integer stockActual = inventarioServicio.obtenerStockActual(productoId);
        Map<String, Object> response = new HashMap<>();
        response.put("productoId", productoId);
        response.put("stockActual", stockActual);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Actualizar ubicación de un producto
     * PATCH /api/inventario/producto/{productoId}/ubicacion
     * Body: { "ubicacion": "Estante A1" }
     */
    @PatchMapping("/producto/{productoId}/ubicacion")
    public ResponseEntity<InventarioResponse> actualizarUbicacion(
            @PathVariable Long productoId,
            @RequestBody Map<String, String> body) {
        
        String nuevaUbicacion = body.get("ubicacion");
        Inventario inventario = inventarioServicio.actualizarUbicacion(productoId, nuevaUbicacion);
        return ResponseEntity.ok(inventarioMapper.toResponse(inventario));
    }
    
    /**
     * Ajustar stock manualmente (usar con precaución)
     * PATCH /api/inventario/producto/{productoId}/ajustar
     * Body: { "cantidad": 50 }
     */
    @PatchMapping("/producto/{productoId}/ajustar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MensajeResponse> ajustarStock(
            @PathVariable Long productoId,
            @RequestBody Map<String, Integer> body) {
        
        Integer nuevaCantidad = body.get("cantidad");
        inventarioServicio.ajustarStock(productoId, nuevaCantidad);
        return ResponseEntity.ok(
            MensajeResponse.of("Stock ajustado exitosamente a " + nuevaCantidad + " unidades")
        );
    }
}