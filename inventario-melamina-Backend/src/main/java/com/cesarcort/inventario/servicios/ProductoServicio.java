package com.cesarcort.inventario.servicios;

import com.cesarcort.inventario.entidades.Categoria;
import com.cesarcort.inventario.entidades.Inventario;
import com.cesarcort.inventario.entidades.Producto;
import com.cesarcort.inventario.entidades.Proveedor;
import com.cesarcort.inventario.excepciones.ConflictoException;
import com.cesarcort.inventario.excepciones.RecursoNoEncontradoException;
import com.cesarcort.inventario.repositorios.CategoriaRepositorio;
import com.cesarcort.inventario.repositorios.InventarioRepositorio;
import com.cesarcort.inventario.repositorios.ProductoRepositorio;
import com.cesarcort.inventario.repositorios.ProveedorRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar productos de melamina/madera
 */
@Service
@RequiredArgsConstructor
public class ProductoServicio {
    
    private final ProductoRepositorio productoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final ProveedorRepositorio proveedorRepositorio;
    private final InventarioRepositorio inventarioRepositorio;
    
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        return productoRepositorio.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Producto> obtenerActivos() {
        return productoRepositorio.findByActivo(true);
    }
    
    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoRepositorio.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Producto", "id", id));
    }
    
    @Transactional(readOnly = true)
    public Producto obtenerPorCodigo(String codigo) {
        return productoRepositorio.findByCodigo(codigo)
            .orElseThrow(() -> new RecursoNoEncontradoException("Producto", "codigo", codigo));
    }
    
    @Transactional
    public Producto crear(Producto producto) {
        // Validar código único
        if (productoRepositorio.existsByCodigo(producto.getCodigo())) {
            throw new ConflictoException("Ya existe un producto con el código: " + producto.getCodigo());
        }
        
        // Validar y asignar categoría
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepositorio.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoria", "id", producto.getCategoria().getId()));
            producto.setCategoria(categoria);
        }
        
        // Validar y asignar proveedor si existe
        if (producto.getProveedor() != null && producto.getProveedor().getId() != null) {
            Proveedor proveedor = proveedorRepositorio.findById(producto.getProveedor().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor", "id", producto.getProveedor().getId()));
            producto.setProveedor(proveedor);
        }
        
        // Guardar producto
        Producto productoGuardado = productoRepositorio.save(producto);
        
        // Crear registro de inventario inicial con cantidad 0
        Inventario inventario = Inventario.builder()
            .producto(productoGuardado)
            .cantidadActual(0)
            .build();
        inventarioRepositorio.save(inventario);
        
        return productoGuardado;
    }
    
    @Transactional
    public Producto actualizar(Long id, Producto productoActualizado) {
        Producto producto = obtenerPorId(id);
        
        // Validar código único si se cambió
        if (!producto.getCodigo().equals(productoActualizado.getCodigo())
            && productoRepositorio.existsByCodigo(productoActualizado.getCodigo())) {
            throw new ConflictoException("Ya existe un producto con el código: " + productoActualizado.getCodigo());
        }
        
        // Actualizar campos
        producto.setCodigo(productoActualizado.getCodigo());
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setColor(productoActualizado.getColor());
        producto.setTextura(productoActualizado.getTextura());
        producto.setEspesor(productoActualizado.getEspesor());
        producto.setLargo(productoActualizado.getLargo());
        producto.setAncho(productoActualizado.getAncho());
        producto.setUnidadMedida(productoActualizado.getUnidadMedida());
        producto.setPrecioCompra(productoActualizado.getPrecioCompra());
        producto.setPrecioVenta(productoActualizado.getPrecioVenta());
        producto.setStockMinimo(productoActualizado.getStockMinimo());
        producto.setActivo(productoActualizado.getActivo());
        
        // Actualizar categoría si cambió
        if (productoActualizado.getCategoria() != null && productoActualizado.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepositorio.findById(productoActualizado.getCategoria().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoria", "id", productoActualizado.getCategoria().getId()));
            producto.setCategoria(categoria);
        }
        
        // Actualizar proveedor si cambió
        if (productoActualizado.getProveedor() != null && productoActualizado.getProveedor().getId() != null) {
            Proveedor proveedor = proveedorRepositorio.findById(productoActualizado.getProveedor().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor", "id", productoActualizado.getProveedor().getId()));
            producto.setProveedor(proveedor);
        }
        
        return productoRepositorio.save(producto);
    }
    
    @Transactional
    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        producto.setActivo(false);
        productoRepositorio.save(producto);
    }
    
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepositorio.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Producto> obtenerPorCategoria(Long categoriaId) {
        return productoRepositorio.findByCategoriaId(categoriaId);
    }
    
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepositorio.findProductosConStockBajo();
    }
    
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosSinStock() {
        return productoRepositorio.findProductosSinStock();
    }
}