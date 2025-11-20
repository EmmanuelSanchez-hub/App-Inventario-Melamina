package com.cesarcort.inventario.servicios;

import com.cesarcort.inventario.entidades.Cliente;
import com.cesarcort.inventario.excepciones.ConflictoException;
import com.cesarcort.inventario.excepciones.RecursoNoEncontradoException;
import com.cesarcort.inventario.repositorios.ClienteRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar clientes
 */
@Service
@RequiredArgsConstructor
public class ClienteServicio {
    
    private final ClienteRepositorio clienteRepositorio;
    
    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodos() {
        return clienteRepositorio.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> obtenerActivos() {
        return clienteRepositorio.findByActivo(true);
    }
    
    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return clienteRepositorio.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", "id", id));
    }
    
    @Transactional
    public Cliente crear(Cliente cliente) {
        // Validar documento único
        if (clienteRepositorio.existsByNumeroDocumento(cliente.getNumeroDocumento())) {
            throw new ConflictoException("Ya existe un cliente con el documento: " + cliente.getNumeroDocumento());
        }
        return clienteRepositorio.save(cliente);
    }
    
    @Transactional
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Cliente cliente = obtenerPorId(id);
        
        // Validar documento único si se cambió
        if (!cliente.getNumeroDocumento().equals(clienteActualizado.getNumeroDocumento())
            && clienteRepositorio.existsByNumeroDocumento(clienteActualizado.getNumeroDocumento())) {
            throw new ConflictoException("Ya existe un cliente con el documento: " + clienteActualizado.getNumeroDocumento());
        }
        
        cliente.setTipoDocumento(clienteActualizado.getTipoDocumento());
        cliente.setNumeroDocumento(clienteActualizado.getNumeroDocumento());
        cliente.setNombres(clienteActualizado.getNombres());
        cliente.setApellidos(clienteActualizado.getApellidos());
        cliente.setRazonSocial(clienteActualizado.getRazonSocial());
        cliente.setTelefono(clienteActualizado.getTelefono());
        cliente.setEmail(clienteActualizado.getEmail());
        cliente.setDireccion(clienteActualizado.getDireccion());
        cliente.setCiudad(clienteActualizado.getCiudad());
        cliente.setActivo(clienteActualizado.getActivo());
        
        return clienteRepositorio.save(cliente);
    }
    
    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);
        cliente.setActivo(false);
        clienteRepositorio.save(cliente);
    }
    
    @Transactional(readOnly = true)
    public Cliente buscarPorDocumento(String numeroDocumento) {
        return clienteRepositorio.findByNumeroDocumento(numeroDocumento)
            .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", "documento", numeroDocumento));
    }
}