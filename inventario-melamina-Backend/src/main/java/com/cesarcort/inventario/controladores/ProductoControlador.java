package com.cesarcort.inventario.controladores;

import com.cesarcort.inventario.dto.mapper.ProductoMapper;
import com.cesarcort.inventario.dto.request.ProductoRequest;
import com.cesarcort.inventario.dto.response.MensajeResponse;
import com.cesarcort.inventario.dto.response.ProductoResponse;
import com.cesarcort.inventario.entidades.Categoria;
import com.cesarcort.inventario.entidades.Producto;
import com.cesarcort.inventario.entidades.Proveedor;
import com.cesarcort.inventario.servicios.ProductoServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestión de productos
 * Accesible por ADMIN, VENDEDOR y ALMACENERO
 */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'ALMACENERO')")
public class ProductoControlador {
    
    private final ProductoServicio productoServicio;
    private final ProductoMapper productoMapper;
    
    /**
     * Obtener todos los productos
     * GET /api/productos
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> obtenerTodos() {
        List<Producto> productos = productoServicio.obtenerTodos();
        List<ProductoResponse> response = productos.stream()
            .map(productoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener productos activos
     * GET /api/productos/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<ProductoResponse>> obtenerActivos() {
        List<Producto> productos = productoServicio.obtenerActivos();
        List<ProductoResponse> response = productos.stream()
            .map(productoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener producto por ID
     * GET /api/productos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoServicio.obtenerPorId(id);
        return ResponseEntity.ok(productoMapper.toResponse(producto));
    }
    
    /**
     * Obtener producto por código
     * GET /api/productos/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoResponse> obtenerPorCodigo(@PathVariable String codigo) {
        Producto producto = productoServicio.obtenerPorCodigo(codigo);
        return ResponseEntity.ok(productoMapper.toResponse(producto));
    }
    
    /**
     * Buscar productos por nombre
     * GET /api/productos/buscar?nombre=melamina
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponse>> buscarPorNombre(@RequestParam String nombre) {
        List<Producto> productos = productoServicio.buscarPorNombre(nombre);
        List<ProductoResponse> response = productos.stream()
            .map(productoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener productos por categoría
     * GET /api/productos/categoria/{categoriaId}
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponse>> obtenerPorCategoria(@PathVariable Long categoriaId) {
        List<Producto> productos = productoServicio.obtenerPorCategoria(categoriaId);
        List<ProductoResponse> response = productos.stream()
            .map(productoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener productos con stock bajo
     * GET /api/productos/stock-bajo
     */
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoResponse>> obtenerProductosConStockBajo() {
        List<Producto> productos = productoServicio.obtenerProductosConStockBajo();
        List<ProductoResponse> response = productos.stream()
            .map(productoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener productos sin stock
     * GET /api/productos/sin-stock
     */
    @GetMapping("/sin-stock")
    public ResponseEntity<List<ProductoResponse>> obtenerProductosSinStock() {
        List<Producto> productos = productoServicio.obtenerProductosSinStock();
        List<ProductoResponse> response = productos.stream()
            .map(productoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Crear nuevo producto
     * POST /api/productos
     * 
     * Body:
     * {
     *   "codigo": "MEL-BLA-18",
     *   "nombre": "Tablero Melamina Blanco",
     *   "descripcion": "Tablero 2.44x1.83m",
     *   "categoriaId": 1,
     *   "color": "Blanco",
     *   "textura": "Lisa",
     *   "espesor": 18.0,
     *   "largo": 244.0,
     *   "ancho": 183.0,
     *   "unidadMedida": "UNIDAD",
     *   "precioCompra": 85.00,
     *   "precioVenta": 120.00,
     *   "stockMinimo": 10,
     *   "proveedorId": 1,
     *   "activo": true
     * }
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        // Construir la categoría (solo con ID)
        Categoria categoria = new Categoria();
        categoria.setId(request.getCategoriaId());
        
        // Construir el proveedor si existe
        Proveedor proveedor = null;
        if (request.getProveedorId() != null) {
            proveedor = new Proveedor();
            proveedor.setId(request.getProveedorId());
        }
        
        // Construir el producto
        Producto producto = Producto.builder()
            .codigo(request.getCodigo())
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .categoria(categoria)
            .color(request.getColor())
            .textura(request.getTextura())
            .espesor(request.getEspesor())
            .largo(request.getLargo())
            .ancho(request.getAncho())
            .unidadMedida(request.getUnidadMedida())
            .precioCompra(request.getPrecioCompra())
            .precioVenta(request.getPrecioVenta())
            .stockMinimo(request.getStockMinimo())
            .proveedor(proveedor)
            .activo(request.getActivo())
            .build();
        
        Producto productoGuardado = productoServicio.crear(producto);
        log.info("Producto creado: {}", productoGuardado.getCodigo());
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(productoMapper.toResponse(productoGuardado));
    }
    
    /**
     * Actualizar producto
     * PUT /api/productos/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {
        
        // Construir la categoría
        Categoria categoria = new Categoria();
        categoria.setId(request.getCategoriaId());
        
        // Construir el proveedor si existe
        Proveedor proveedor = null;
        if (request.getProveedorId() != null) {
            proveedor = new Proveedor();
            proveedor.setId(request.getProveedorId());
        }
        
        // Construir el producto
        Producto producto = Producto.builder()
            .codigo(request.getCodigo())
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .categoria(categoria)
            .color(request.getColor())
            .textura(request.getTextura())
            .espesor(request.getEspesor())
            .largo(request.getLargo())
            .ancho(request.getAncho())
            .unidadMedida(request.getUnidadMedida())
            .precioCompra(request.getPrecioCompra())
            .precioVenta(request.getPrecioVenta())
            .stockMinimo(request.getStockMinimo())
            .proveedor(proveedor)
            .activo(request.getActivo())
            .build();
        
        Producto productoActualizado = productoServicio.actualizar(id, producto);
        log.info("Producto actualizado: {}", productoActualizado.getCodigo());
        
        return ResponseEntity.ok(productoMapper.toResponse(productoActualizado));
    }
    
    /**
     * Eliminar (desactivar) producto
     * DELETE /api/productos/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MensajeResponse> eliminar(@PathVariable Long id) {
        productoServicio.eliminar(id);
        log.info("Producto desactivado: ID {}", id);
        return ResponseEntity.ok(
            MensajeResponse.of("Producto desactivado exitosamente")
        );
    }
}