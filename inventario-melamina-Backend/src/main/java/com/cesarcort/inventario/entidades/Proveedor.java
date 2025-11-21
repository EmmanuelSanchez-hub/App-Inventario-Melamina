package com.cesarcort.inventario.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "razon_social", nullable = false)
    private String razonSocial;
    
    @Column(unique = true, length = 20)
    private String ruc;
    
    @Column(length = 100)
    private String contacto;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(length = 100)
    private String email;
    
    @Column(columnDefinition = "TEXT")
    private String direccion;
    
    @Column(length = 100)
    private String ciudad;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();
    
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Movimiento> movimientos = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}