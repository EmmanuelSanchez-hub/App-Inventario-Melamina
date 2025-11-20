package com.cesarcort.inventario.controladores;

import com.cesarcort.inventario.dto.mapper.CategoriaMapper;
import com.cesarcort.inventario.dto.request.CategoriaRequest;
import com.cesarcort.inventario.dto.response.CategoriaResponse;
import com.cesarcort.inventario.dto.response.MensajeResponse;
import com.cesarcort.inventario.entidades.Categoria;
import com.cesarcort.inventario.servicios.CategoriaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestión de categorías
 */
@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'ALMACENERO')")
public class CategoriaControlador {
    
    private final CategoriaServicio categoriaServicio;
    private final CategoriaMapper categoriaMapper;
    
    /**
     * Obtener todas las categorías
     * GET /api/categorias
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obtenerTodas() {
        List<Categoria> categorias = categoriaServicio.obtenerTodas();
        List<CategoriaResponse> response = categorias.stream()
            .map(categoriaMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener categorías activas
     * GET /api/categorias/activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaResponse>> obtenerActivas() {
        List<Categoria> categorias = categoriaServicio.obtenerActivas();
        List<CategoriaResponse> response = categorias.stream()
            .map(categoriaMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener categoría por ID
     * GET /api/categorias/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable Long id) {
        Categoria categoria = categoriaServicio.obtenerPorId(id);
        return ResponseEntity.ok(categoriaMapper.toResponse(categoria));
    }
    
    /**
     * Buscar categorías por nombre
     * GET /api/categorias/buscar?nombre=melamina
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponse>> buscarPorNombre(@RequestParam String nombre) {
        List<Categoria> categorias = categoriaServicio.buscarPorNombre(nombre);
        List<CategoriaResponse> response = categorias.stream()
            .map(categoriaMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Crear nueva categoría
     * POST /api/categorias
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        Categoria categoria = Categoria.builder()
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .activo(request.getActivo())
            .build();
        
        Categoria categoriaGuardada = categoriaServicio.crear(categoria);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(categoriaMapper.toResponse(categoriaGuardada));
    }
    
    /**
     * Actualizar categoría
     * PUT /api/categorias/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {
        
        Categoria categoria = Categoria.builder()
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .activo(request.getActivo())
            .build();
        
        Categoria categoriaActualizada = categoriaServicio.actualizar(id, categoria);
        return ResponseEntity.ok(categoriaMapper.toResponse(categoriaActualizada));
    }
    
    /**
     * Eliminar (desactivar) categoría
     * DELETE /api/categorias/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MensajeResponse> eliminar(@PathVariable Long id) {
        categoriaServicio.eliminar(id);
        return ResponseEntity.ok(
            MensajeResponse.of("Categoría desactivada exitosamente")
        );
    }
}