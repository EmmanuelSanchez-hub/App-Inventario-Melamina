package com.cesarcort.inventario.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    // Características específicas de melamina/madera
    @Column(length = 100)
    private String color;
    
    @Column(length = 100)
    private String textura;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal espesor;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal largo;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal ancho;
    
    @Column(name = "unidad_medida", length = 20)
    @Builder.Default
    private String unidadMedida = "UNIDAD"; // UNIDAD, M2, ML, KG
    
    // Precios y stock
    @Column(name = "precio_compra", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal precioCompra = BigDecimal.ZERO;
    
    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal precioVenta = BigDecimal.ZERO;
    
    @Column(name = "stock_minimo")
    @Builder.Default
    private Integer stockMinimo = 5;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
    
    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL)
    private Inventario inventario;
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Movimiento> movimientos = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}