package com.cesarcort.inventario.controladores;

import com.cesarcort.inventario.dto.mapper.ProveedorMapper;
import com.cesarcort.inventario.dto.request.ProveedorRequest;
import com.cesarcort.inventario.dto.response.MensajeResponse;
import com.cesarcort.inventario.dto.response.ProveedorResponse;
import com.cesarcort.inventario.entidades.Proveedor;
import com.cesarcort.inventario.servicios.ProveedorServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestión de proveedores
 */
@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
public class ProveedorControlador {
    
    private final ProveedorServicio proveedorServicio;
    private final ProveedorMapper proveedorMapper;
    
    @GetMapping
    public ResponseEntity<List<ProveedorResponse>> obtenerTodos() {
        List<Proveedor> proveedores = proveedorServicio.obtenerTodos();
        List<ProveedorResponse> response = proveedores.stream()
            .map(proveedorMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<ProveedorResponse>> obtenerActivos() {
        List<Proveedor> proveedores = proveedorServicio.obtenerActivos();
        List<ProveedorResponse> response = proveedores.stream()
            .map(proveedorMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponse> obtenerPorId(@PathVariable Long id) {
        Proveedor proveedor = proveedorServicio.obtenerPorId(id);
        return ResponseEntity.ok(proveedorMapper.toResponse(proveedor));
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<ProveedorResponse>> buscarPorRazonSocial(@RequestParam String razonSocial) {
        List<Proveedor> proveedores = proveedorServicio.buscarPorRazonSocial(razonSocial);
        List<ProveedorResponse> response = proveedores.stream()
            .map(proveedorMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<ProveedorResponse> crear(@Valid @RequestBody ProveedorRequest request) {
        Proveedor proveedor = Proveedor.builder()
            .razonSocial(request.getRazonSocial())
            .ruc(request.getRuc())
            .contacto(request.getContacto())
            .telefono(request.getTelefono())
            .email(request.getEmail())
            .direccion(request.getDireccion())
            .ciudad(request.getCiudad())
            .activo(request.getActivo())
            .build();
        
        Proveedor proveedorGuardado = proveedorServicio.crear(proveedor);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(proveedorMapper.toResponse(proveedorGuardado));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorRequest request) {
        
        Proveedor proveedor = Proveedor.builder()
            .razonSocial(request.getRazonSocial())
            .ruc(request.getRuc())
            .contacto(request.getContacto())
            .telefono(request.getTelefono())
            .email(request.getEmail())
            .direccion(request.getDireccion())
            .ciudad(request.getCiudad())
            .activo(request.getActivo())
            .build();
        
        Proveedor proveedorActualizado = proveedorServicio.actualizar(id, proveedor);
        return ResponseEntity.ok(proveedorMapper.toResponse(proveedorActualizado));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> eliminar(@PathVariable Long id) {
        proveedorServicio.eliminar(id);
        return ResponseEntity.ok(
            MensajeResponse.of("Proveedor desactivado exitosamente")
        );
    }
}