package com.cesarcort.inventario.controladores;

import com.cesarcort.inventario.dto.mapper.ClienteMapper;
import com.cesarcort.inventario.dto.request.ClienteRequest;
import com.cesarcort.inventario.dto.response.ClienteResponse;
import com.cesarcort.inventario.dto.response.MensajeResponse;
import com.cesarcort.inventario.entidades.Cliente;
import com.cesarcort.inventario.servicios.ClienteServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestión de clientes
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'ALMACENERO')")
public class ClienteControlador {
    
    private final ClienteServicio clienteServicio;
    private final ClienteMapper clienteMapper;
    
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> obtenerTodos() {
        List<Cliente> clientes = clienteServicio.obtenerTodos();
        List<ClienteResponse> response = clientes.stream()
            .map(clienteMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<ClienteResponse>> obtenerActivos() {
        List<Cliente> clientes = clienteServicio.obtenerActivos();
        List<ClienteResponse> response = clientes.stream()
            .map(clienteMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteServicio.obtenerPorId(id);
        return ResponseEntity.ok(clienteMapper.toResponse(cliente));
    }
    
    @GetMapping("/documento/{numeroDocumento}")
    public ResponseEntity<ClienteResponse> obtenerPorDocumento(@PathVariable String numeroDocumento) {
        Cliente cliente = clienteServicio.buscarPorDocumento(numeroDocumento);
        return ResponseEntity.ok(clienteMapper.toResponse(cliente));
    }
    
    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        Cliente cliente = Cliente.builder()
            .tipoDocumento(request.getTipoDocumento())
            .numeroDocumento(request.getNumeroDocumento())
            .nombres(request.getNombres())
            .apellidos(request.getApellidos())
            .razonSocial(request.getRazonSocial())
            .telefono(request.getTelefono())
            .email(request.getEmail())
            .direccion(request.getDireccion())
            .ciudad(request.getCiudad())
            .activo(request.getActivo())
            .build();
        
        Cliente clienteGuardado = clienteServicio.crear(cliente);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(clienteMapper.toResponse(clienteGuardado));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        
        Cliente cliente = Cliente.builder()
            .tipoDocumento(request.getTipoDocumento())
            .numeroDocumento(request.getNumeroDocumento())
            .nombres(request.getNombres())
            .apellidos(request.getApellidos())
            .razonSocial(request.getRazonSocial())
            .telefono(request.getTelefono())
            .email(request.getEmail())
            .direccion(request.getDireccion())
            .ciudad(request.getCiudad())
            .activo(request.getActivo())
            .build();
        
        Cliente clienteActualizado = clienteServicio.actualizar(id, cliente);
        return ResponseEntity.ok(clienteMapper.toResponse(clienteActualizado));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> eliminar(@PathVariable Long id) {
        clienteServicio.eliminar(id);
        return ResponseEntity.ok(
            MensajeResponse.of("Cliente desactivado exitosamente")
        );
    }
}