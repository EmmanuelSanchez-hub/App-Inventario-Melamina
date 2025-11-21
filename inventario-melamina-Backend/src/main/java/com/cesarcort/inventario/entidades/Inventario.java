package com.cesarcort.inventario.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false, unique = true)
    private Producto producto;
    
    @Column(name = "cantidad_actual", nullable = false)
    @Builder.Default
    private Integer cantidadActual = 0;
    
    @Column(length = 100)
    private String ubicacion;
    
    @UpdateTimestamp
    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;
}