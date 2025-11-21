package com.cesarcort.inventario.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tipo_documento", length = 20)
    @Builder.Default
    private String tipoDocumento = "DNI"; // DNI, RUC, CE
    
    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    private String numeroDocumento;
    
    @Column(length = 100)
    private String nombres;
    
    @Column(length = 100)
    private String apellidos;
    
    @Column(name = "razon_social")
    private String razonSocial;
    
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
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Movimiento> movimientos = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}