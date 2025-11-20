package com.cesarcort.inventario.servicios;

import com.cesarcort.inventario.entidades.Proveedor;
import com.cesarcort.inventario.excepciones.ConflictoException;
import com.cesarcort.inventario.excepciones.RecursoNoEncontradoException;
import com.cesarcort.inventario.repositorios.ProveedorRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar proveedores
 */
@Service
@RequiredArgsConstructor
public class ProveedorServicio {
    
    private final ProveedorRepositorio proveedorRepositorio;
    
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerTodos() {
        return proveedorRepositorio.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerActivos() {
        return proveedorRepositorio.findByActivo(true);
    }
    
    @Transactional(readOnly = true)
    public Proveedor obtenerPorId(Long id) {
        return proveedorRepositorio.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor", "id", id));
    }
    
    @Transactional
    public Proveedor crear(Proveedor proveedor) {
        // Validar RUC único si se proporciona
        if (proveedor.getRuc() != null && proveedorRepositorio.existsByRuc(proveedor.getRuc())) {
            throw new ConflictoException("Ya existe un proveedor con el RUC: " + proveedor.getRuc());
        }
        return proveedorRepositorio.save(proveedor);
    }
    
    @Transactional
    public Proveedor actualizar(Long id, Proveedor proveedorActualizado) {
        Proveedor proveedor = obtenerPorId(id);
        
        // Validar RUC único si se cambió
        if (proveedorActualizado.getRuc() != null 
            && !proveedorActualizado.getRuc().equals(proveedor.getRuc())
            && proveedorRepositorio.existsByRuc(proveedorActualizado.getRuc())) {
            throw new ConflictoException("Ya existe un proveedor con el RUC: " + proveedorActualizado.getRuc());
        }
        
        proveedor.setRazonSocial(proveedorActualizado.getRazonSocial());
        proveedor.setRuc(proveedorActualizado.getRuc());
        proveedor.setContacto(proveedorActualizado.getContacto());
        proveedor.setTelefono(proveedorActualizado.getTelefono());
        proveedor.setEmail(proveedorActualizado.getEmail());
        proveedor.setDireccion(proveedorActualizado.getDireccion());
        proveedor.setCiudad(proveedorActualizado.getCiudad());
        proveedor.setActivo(proveedorActualizado.getActivo());
        
        return proveedorRepositorio.save(proveedor);
    }
    
    @Transactional
    public void eliminar(Long id) {
        Proveedor proveedor = obtenerPorId(id);
        proveedor.setActivo(false);
        proveedorRepositorio.save(proveedor);
    }
    
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorRazonSocial(String razonSocial) {
        return proveedorRepositorio.findByRazonSocialContainingIgnoreCase(razonSocial);
    }
}