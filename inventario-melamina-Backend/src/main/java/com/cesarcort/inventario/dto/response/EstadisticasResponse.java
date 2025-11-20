package com.cesarcort.inventario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para estadísticas del sistema
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticasResponse {
    
    private Long totalProductos;
    private Long productosActivos;
    private Long productosSinStock;
    private Long productosStockBajo;
    
    private Double valorTotalInventario;
    private Double totalVentasMes;
    private Double totalComprasMes;
    
    private Long totalClientes;
    private Long totalProveedores;
    private Long totalMovimientosMes;
}